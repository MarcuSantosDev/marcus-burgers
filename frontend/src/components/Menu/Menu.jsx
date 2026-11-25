import SectionHeading from "../SectionHeading/SectionHeading";
import ProductCard from "../ProductCard/ProductCard";
import "./Menu.css";

function Menu({ products = [], onAddToCart, isAdmin = false }) {
  return (
    <section className="menu" id="cardapio" aria-labelledby="menu-title">
      <SectionHeading
        id="menu-title"
        eyebrow="NOSSO CARDÁPIO"
        title="Escolha seu favorito"
        description="Combinações especiais preparadas para surpreender você."
      />

      <div className="menu__categories">
        {Array.from(
          new Map(
            products.map((product) => [
              product.category?.name || "Sem categoria",
              product.category?.name || "Sem categoria",
            ])
          ).keys()
        ).map((categoryName) => {
          const categoryProducts = products.filter(
            (product) => (product.category?.name || "Sem categoria") === categoryName
          );

          return (
            <section className="menu__category" key={categoryName}>
              <h3 className="menu__category-title">{categoryName}</h3>
              <div className="menu__grid">
                {categoryProducts.map((product) => (
                  <ProductCard
                    key={product.id}
                    product={product}
                    onAdd={onAddToCart}
                    isAdmin={isAdmin}
                  />
                ))}
              </div>
            </section>
          );
        })}
      </div>
    </section>
  );
}

export default Menu;