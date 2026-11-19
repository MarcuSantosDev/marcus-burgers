import Logo from "../Logo/Logo";
import { BRAND } from "../../data/site";
import "./Footer.css";

function Footer() {
  const year = new Date().getFullYear();

  return (
    <footer className="footer">
      <Logo />
      <p className="footer__copy">
        © {year} {BRAND.name}. Todos os direitos reservados.
      </p>
    </footer>
  );
}

export default Footer;