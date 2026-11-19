import SectionHeading from "../SectionHeading/SectionHeading";
import ProductCard from "../ProductCard/ProductCard";
import { PRODUCTS } from "../../data/products";
import "./Menu.css";

function Menu({ onAddToCart }) {
  return (
    <section className="menu" id="cardapio" aria-labelledby="menu-title">
      <SectionHeading
        id="menu-title"
        eyebrow="NOSSO CARDÁPIO"
        title="Escolha seu favorito"
        description="Combinações especiais preparadas para surpreender você."
      />

      <div className="menu__grid">
        {PRODUCTS.map((product) => (
          <ProductCard
            key={product.id}
            product={product}
            onAdd={onAddToCart}
          />
        ))}
      </div>
    </section>
  );
}

export default Menu;