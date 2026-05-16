import { Html5QrcodeScanner } from 'html5-qrcode'
import { useEffect, useId, useRef } from 'react'

interface QRScannerProps {
  onScan: (value: string) => void
}

export function QRScanner({ onScan }: QRScannerProps): React.JSX.Element {
  const id = useId().replaceAll(':', '')
  const scannerRef = useRef<Html5QrcodeScanner | null>(null)
  const onScanRef = useRef(onScan)

  useEffect(() => {
    onScanRef.current = onScan
  }, [onScan])

  useEffect(() => {
    const scanner = new Html5QrcodeScanner(
      id,
      {
        fps: 10,
        qrbox: { width: 260, height: 260 },
        rememberLastUsedCamera: true,
      },
      false,
    )
    scannerRef.current = scanner
    scanner.render(
      (decodedText) => {
        onScanRef.current(decodedText)
      },
      () => undefined,
    )

    return () => {
      scannerRef.current?.clear().catch(() => undefined)
      scannerRef.current = null
    }
  }, [id])

  return <div className="scanner-shell" id={id} />
}
