const QR_SEGMENT = ':qr:'

export function normalizeValidationReference(input: string): string {
  return input.trim()
}

/** Debounce key for repeated scans of the same ticket. */
export function validationReferenceKey(rawValue: string): string {
  const trimmed = normalizeValidationReference(rawValue)
  if (trimmed.includes(QR_SEGMENT)) {
    return trimmed.split(QR_SEGMENT).at(1) ?? trimmed
  }
  return trimmed
}

export const MANUAL_VALIDATION_HELP =
  'Paste a ticket UUID, QR code UUID, or the full scan payload (booktheshow:ticket:...:qr:...).'

export const MANUAL_VALIDATION_PLACEHOLDER = 'e.g. ticket UUID or booktheshow:ticket:...:qr:...'
