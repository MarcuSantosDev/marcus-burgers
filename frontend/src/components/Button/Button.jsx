import "./Button.css";

/**
 * Link estilizado como botão.
 * variant: "primary" (preenchido) | "link" (texto discreto)
 */
function Button({ href, variant = "primary", className = "", children, ...rest }) {
  const classes = ["btn", `btn--${variant}`, className].filter(Boolean).join(" ");

  return (
    <a href={href} className={classes} {...rest}>
      {children}
    </a>
  );
}

export default Button;