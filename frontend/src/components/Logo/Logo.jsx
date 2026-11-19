import { BRAND } from "../../data/site";
import "./Logo.css";

function Logo({ href = "#inicio" }) {
  return (
    <a href={href} className="logo" aria-label={`${BRAND.name} - início`}>
      {BRAND.logoMain}
      <span className="logo__accent">{BRAND.logoAccent}</span>
      <small className="logo__sub">{BRAND.logoSub}</small>
    </a>
  );
}

export default Logo;