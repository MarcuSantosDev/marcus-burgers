import { unsplashUrl } from "./unsplash";

/** Aceita produto com `image` (URL completa) ou `imageId` (Unsplash). */
export function getProductImageUrl(product, width) {
  return product.imageUrl ?? product.image ?? unsplashUrl(product.imageId, width);
}