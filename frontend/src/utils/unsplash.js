const BASE_URL = "https://images.unsplash.com";

export function unsplashUrl(imageId, width) {
  return `${BASE_URL}/${imageId}?w=${width}`;
}

export function unsplashSrcSet(imageId, widths) {
  return widths.map((w) => `${unsplashUrl(imageId, w)} ${w}w`).join(", ");
}