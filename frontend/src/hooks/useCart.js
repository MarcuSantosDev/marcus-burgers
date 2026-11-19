import { useEffect, useMemo, useReducer } from "react";
import { PRODUCTS } from "../data/products";

const STORAGE_KEY = "marcuss-burgers:cart";

/**
 * O estado guarda só { id, quantity }. Nome, preço e imagem são lidos de
 * PRODUCTS a cada render, então mudanças de preço refletem no carrinho.
 */
function cartReducer(state, action) {
  switch (action.type) {
    case "add": {
      const exists = state.some((line) => line.id === action.id);
      return exists
        ? state.map((line) =>
            line.id === action.id
              ? { ...line, quantity: line.quantity + 1 }
              : line,
          )
        : [...state, { id: action.id, quantity: 1 }];
    }
    case "increment":
      return state.map((line) =>
        line.id === action.id ? { ...line, quantity: line.quantity + 1 } : line,
      );
    case "decrement":
      return state
        .map((line) =>
          line.id === action.id
            ? { ...line, quantity: line.quantity - 1 }
            : line,
        )
        .filter((line) => line.quantity > 0);
    case "remove":
      return state.filter((line) => line.id !== action.id);
    case "clear":
      return [];
    default:
      return state;
  }
}

function loadStoredCart() {
  try {
    const parsed = JSON.parse(localStorage.getItem(STORAGE_KEY));
    if (!Array.isArray(parsed)) return [];

    return parsed.filter(
      (line) =>
        Number.isInteger(line?.id) &&
        Number.isInteger(line?.quantity) &&
        line.quantity > 0,
    );
  } catch {
    return [];
  }
}

export function useCart() {
  const [lines, dispatch] = useReducer(cartReducer, undefined, loadStoredCart);

  useEffect(() => {
    try {
      localStorage.setItem(STORAGE_KEY, JSON.stringify(lines));
    } catch {
      // localStorage indisponível (modo privado, cota cheia): segue sem salvar.
    }
  }, [lines]);

  const items = useMemo(
    () =>
      lines.flatMap(({ id, quantity }) => {
        const product = PRODUCTS.find((p) => p.id === id);
        return product ? [{ product, quantity }] : [];
      }),
    [lines],
  );

  const totalItems = items.reduce((sum, item) => sum + item.quantity, 0);

  // Soma em centavos para evitar erros de ponto flutuante (ex.: 29.9 * 3).
  const totalPrice =
    items.reduce(
      (sum, { product, quantity }) =>
        sum + Math.round(product.price * 100) * quantity,
      0,
    ) / 100;

  return {
    items,
    totalItems,
    totalPrice,
    add: (id) => dispatch({ type: "add", id }),
    increment: (id) => dispatch({ type: "increment", id }),
    decrement: (id) => dispatch({ type: "decrement", id }),
    remove: (id) => dispatch({ type: "remove", id }),
    clear: () => dispatch({ type: "clear" }),
  };
}