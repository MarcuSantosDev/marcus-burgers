import { formatCurrency } from "../../utils/formatCurrency";
import { unsplashUrl, unsplashSrcSet } from "../../utils/unsplash";
import "./ProductCard.css";

function ProductCard({ product, onAdd, isAdmin = false }) {
  const { name, description, price, imageUrl, imageId, stock = 0 } = product;

  return (
    <article className="product-card">
      <img
        className="product-card__image"
        src={imageUrl || unsplashUrl(imageId, 600)}
        srcSet={imageUrl ? undefined : unsplashSrcSet(imageId, [400, 600, 900])}
        sizes="(max-width: 600px) 100vw, (max-width: 900px) 50vw, 33vw"
        width="600"
        height="220"
        alt={name}
        loading="lazy"
      />

      <div className="product-card__body">
        <h3 className="product-card__name">{name}</h3>
        <p className="product-card__description">{description}</p>

        <div className="product-card__footer">
          <strong className="product-card__price">{formatCurrency(price)}</strong>
          {!isAdmin && (
            <button
              type="button"
              className="product-card__button"
              onClick={(event) => onAdd?.(product, event.currentTarget)}
              aria-label={`Adicionar ${name} ao pedido`}
              disabled={stock < 1}
              title={stock < 1 ? "Produto sem estoque" : `${stock} unidade(s) disponível(is)`}
            >
              {stock < 1 ? "Sem estoque" : "Adicionar +"}
            </button>
          )}
        </div>
      </div>
    </article>
  );
}

export default ProductCard;