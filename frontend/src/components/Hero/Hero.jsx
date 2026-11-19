import Button from "../Button/Button";
import { HERO_IMAGE_ID, HERO_STATS } from "../../data/site";
import { unsplashUrl, unsplashSrcSet } from "../../utils/unsplash";
import "./Hero.css";

function Hero() {
  return (
    <section className="hero" id="inicio">
      <div className="hero__content">
        <span className="eyebrow">
          <span aria-hidden="true">🍔 </span>O verdadeiro sabor artesanal
        </span>

        <h1 className="hero__title">
          <span className="hero__title-line">O hambúrguer</span>
          <span className="hero__title-line">perfeito existe.</span>
          <span className="hero__title-line hero__title-line--accent">
            E está aqui!
          </span>
        </h1>

        <p className="hero__text">
          Ingredientes selecionados, carne artesanal e muito sabor em cada
          mordida. Experimente o verdadeiro burger!
        </p>

        <div className="hero__actions">
          <Button href="#cardapio">Ver cardápio →</Button>
          <Button href="#sobre" variant="link">
            Conheça nossa história
          </Button>
        </div>

        <dl className="hero__stats">
          {HERO_STATS.map(({ value, label }) => (
            <div key={label} className="hero__stat">
              <dt className="hero__stat-value">{value}</dt>
              <dd className="hero__stat-label">{label}</dd>
            </div>
          ))}
        </dl>
      </div>

      <div className="hero__media">
        <img
          className="hero__image"
          src={unsplashUrl(HERO_IMAGE_ID, 1000)}
          srcSet={unsplashSrcSet(HERO_IMAGE_ID, [600, 1000, 1400])}
          sizes="(max-width: 900px) 100vw, 50vw"
          width="1000"
          height="450"
          alt="Hambúrguer artesanal com queijo derretido"
        />
      </div>
    </section>
  );
}

export default Hero;