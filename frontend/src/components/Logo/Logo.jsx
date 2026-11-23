import { Link } from "react-router-dom";
import { BRAND } from "../../data/site";
import "./Logo.css";

function Logo() {
  return (
    <Link to="/" className="logo" aria-label={`${BRAND.name} - início`}>
      {BRAND.logoMain}
      <span className="logo__accent">{BRAND.logoAccent}</span>
      <small className="logo__sub">{BRAND.logoSub}</small>
    </Link>
  );
}

export default Logo;