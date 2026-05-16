import { QRCodeSVG } from 'qrcode.react'

interface QRDisplayProps {
  value: string
  title: string
}

export function QRDisplay({ value, title }: QRDisplayProps): React.JSX.Element {
  function downloadQr(): void {
    const link = document.createElement('a')
    link.href = value
    link.download = `${title.toLowerCase().replaceAll(' ', '-')}.png`
    link.click()
  }

  return (
    <section className="qr-display" aria-label="Ticket QR code">
      {value.startsWith('data:image') ? <img src={value} alt={title} /> : <QRCodeSVG value={value} size={220} />}
      <button className="button secondary" type="button" onClick={downloadQr}>
        Download QR
      </button>
    </section>
  )
}
