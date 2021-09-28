export function checkPattern(pattern, string) {
  if (string.match(pattern) != null) {
    if (string.match(pattern)[0].length === string.length) return true;
  }
  return false;
}
