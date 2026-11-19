import "./SectionHeading.css";

function SectionHeading({ eyebrow, title, description, id }) {
  return (
    <div className="section-heading">
      <span className="eyebrow">{eyebrow}</span>
      <h2 className="section-title" id={id}>
        {title}
      </h2>
      {description && <p className="section-heading__text">{description}</p>}
    </div>
  );
}

export default SectionHeading;
