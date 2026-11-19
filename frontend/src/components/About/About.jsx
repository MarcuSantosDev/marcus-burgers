import Button from "../Button/Button";
import { BRAND } from "../../data/site";
import "./About.css";

function About() {
  return (
    <section className="about" id="sobre" aria-labelledby="about-title">
      <span className="eyebrow">
        <span aria-hidden="true">❤️ </span>FEITO COM PAIXÃO
      </span>

      <h2 className="section-title about__title" id="about-title">
        Não é só um hambúrguer. É uma experiência.
      </h2>

      <p className="about__text">
        Na {BRAND.name}, cada detalhe importa. Da escolha dos ingredientes ao
        preparo, nossa missão é entregar sabor de verdade em cada pedido.
      </p>

      <Button href="#cardapio">Quero experimentar →</Button>
    </section>
  );
}

export default About;