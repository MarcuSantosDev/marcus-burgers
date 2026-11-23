import { useEffect, useRef } from "react";
import { Link } from "react-router-dom";
import Button from "../Button/Button";
import { formatCurrency } from "../../utils/formatCurrency";
import { getProductImageUrl } from "../../utils/productImage";
import "./CartDrawer.css";

const FOCUSABLE = 'button, a[href], [tabindex]:not([tabindex="-1"])';

function CartDrawer({
  open,
  onClose,
  items,
  totalItems,
  totalPrice,
  onIncrement,
  onDecrement,
  onRemove,
  onClear,
}) {
  const panelRef = useRef(null);
  const closeRef = useRef(null);
  const onCloseRef = useRef(onClose);

  useEffect(() => {
    onCloseRef.current = onClose;
  });

  // Ao abrir: trava o scroll da página, foca o painel, fecha com Esc,
  // mantém o Tab dentro do painel e devolve o foco ao fechar.
  useEffect(() => {
    if (!open) return;

    const previouslyFocused = document.activeElement;
    const previousOverflow = document.body.style.overflow;
    document.body.style.overflow = "hidden";
    closeRef.current?.focus();

    function handleKeyDown(event) {
      if (event.key === "Escape") {
        onCloseRef.current();
        return;
      }

      if (event.key !== "Tab") return;

      const focusable = panelRef.current?.querySelectorAll(FOCUSABLE);
      if (!focusable?.length) return;

      const first = focusable[0];
      const last = focusable[focusable.length - 1];

      if (event.shiftKey && document.activeElement === first) {
        event.preventDefault();
        last.focus();
      } else if (!event.shiftKey && document.activeElement === last) {
        event.preventDefault();
        first.focus();
      }
    }

    document.addEventListener("keydown", handleKeyDown);

    return () => {
      document.removeEventListener("keydown", handleKeyDown);
      document.body.style.overflow = previousOverflow;
      previouslyFocused?.focus?.({ preventScroll: true });
    };
  }, [open]);

  if (!open) return null;

  return (
    <div className="cart">
      <div className="cart__backdrop" onClick={onClose} />

      <aside
        className="cart__panel"
        role="dialog"
        aria-modal="true"
        aria-labelledby="cart-title"
        ref={panelRef}
      >
        <header className="cart__header">
          <h2 className="cart__title" id="cart-title">
            Seu pedido
          </h2>
          <button
            type="button"
            className="cart__close"
            onClick={onClose}
            aria-label="Fechar carrinho"
            ref={closeRef}
          >
            ×
          </button>
        </header>

        {items.length === 0 ? (
          <div className="cart__empty">
            <p>Seu carrinho está vazio.</p>
            <Button href="#cardapio" onClick={onClose}>
              Ver cardápio
            </Button>
          </div>
        ) : (
          <>
            <ul className="cart__list">
              {items.map(({ product, quantity }) => (
                <li className="cart__item" key={product.id}>
                  <img
                    className="cart__item-image"
                    src={getProductImageUrl(product, 160)}
                    alt={product.name}
                    width="64"
                    height="64"
                    referrerPolicy="no-referrer"
                  />

                  <div className="cart__item-info">
                    <h3 className="cart__item-name">{product.name}</h3>
                    <span className="cart__item-price">
                      {formatCurrency(product.price)}
                    </span>

                    <div
                      className="cart__qty"
                      role="group"
                      aria-label={`Quantidade de ${product.name}`}
                    >
                      <button
                        type="button"
                        onClick={() => onDecrement(product.id)}
                        aria-label={`Diminuir quantidade de ${product.name}`}
                      >
                        −
                      </button>
                      <span aria-live="polite">{quantity}</span>
                      <button
                        type="button"
                        onClick={() => onIncrement(product.id)}
                        aria-label={`Aumentar quantidade de ${product.name}`}
                      >
                        +
                      </button>
                    </div>
                  </div>

                  <div className="cart__item-side">
                    <strong className="cart__item-subtotal">
                      {formatCurrency(product.price * quantity)}
                    </strong>
                    <button
                      type="button"
                      className="cart__remove"
                      onClick={() => onRemove(product.id)}
                      aria-label={`Remover ${product.name} do carrinho`}
                    >
                      Remover
                    </button>
                  </div>
                </li>
              ))}
            </ul>

            <footer className="cart__footer">
              <div className="cart__total">
                <span>
                  Total ({totalItems} {totalItems === 1 ? "item" : "itens"})
                </span>
                <strong>{formatCurrency(totalPrice)}</strong>
              </div>

              <button type="button" className="cart__clear" onClick={onClear}>
                Limpar carrinho
              </button>
              <Link className="primary-button cart__checkout" to="/checkout" onClick={onClose}>
                Finalizar pedido
              </Link>
            </footer>
          </>
        )}
      </aside>
    </div>
  );
}

export default CartDrawer;