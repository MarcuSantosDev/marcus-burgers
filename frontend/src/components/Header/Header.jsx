import { Link } from "react-router-dom";
import Logo from "../Logo/Logo";
import Button from "../Button/Button";
import { NAV_LINKS } from "../../data/site";
import "./Header.css";

function CartIcon() {
  return (
    <svg
      width="20"
      height="20"
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
      aria-hidden="true"
    >
      <circle cx="9" cy="21" r="1" />
      <circle cx="20" cy="21" r="1" />
      <path d="M1 1h4l2.7 13.4a2 2 0 0 0 2 1.6h9.7a2 2 0 0 0 2-1.6L23 6H6" />
    </svg>
  );
}

function Header({ cartCount = 0, onOpenCart, authenticated = false, onLogout }) {
  return (
    <header className="header">
      <Logo />

      <nav className="header__nav" aria-label="Navegação principal">
        {NAV_LINKS.map(({ label, href }) => (
          <Link key={href} to={href} className="header__link">
            {label}
          </Link>
        ))}
      </nav>

      <div className="header__actions">
        {authenticated ? (
          <>
            <Link className="header__auth" to="/pedidos">
              Pedidos
            </Link>
            <button className="header__auth" type="button" onClick={onLogout}>
              Sair
            </button>
          </>
        ) : (
          <Link className="header__auth" to="/login">
            Entrar
          </Link>
        )}

        <button
          type="button"
          className="header__cart"
          onClick={onOpenCart}
          aria-label={`Abrir carrinho, ${cartCount} ${
            cartCount === 1 ? "item" : "itens"
          }`}
        >
          <CartIcon />
          {cartCount > 0 && (
            <span className="header__cart-count" key={cartCount}>
              {cartCount}
            </span>
          )}
        </button>

        <Button href="/checkout" className="header__cta">
          Fazer pedido
        </Button>
      </div>
    </header>
  );
}

export default Header;