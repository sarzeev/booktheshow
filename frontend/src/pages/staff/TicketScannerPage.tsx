import { useCallback, useEffect, useRef, useState } from 'react'
import { useForm } from 'react-hook-form'
import { useParams } from 'react-router-dom'
import { getApiErrorMessage } from '../../api/client'
import type { TicketValidationResponse } from '../../api/types'
import { ErrorState } from '../../components/ErrorState'
import { QRScanner } from '../../components/QRScanner'
import { StatusBadge } from '../../components/StatusBadge'
import { validationService } from '../../services/validationService'
import { formatDateTime } from '../../utils/format'
import {
  MANUAL_VALIDATION_HELP,
  MANUAL_VALIDATION_PLACEHOLDER,
  normalizeValidationReference,
  validationReferenceKey,
} from '../../utils/validationReference'

interface ManualForm {
  reference: string
}

export function TicketScannerPage(): React.JSX.Element {
  const { eventId = '' } = useParams()
  const [result, setResult] = useState<TicketValidationResponse | null>(null)
  const [history, setHistory] = useState<TicketValidationResponse[]>([])
  const [error, setError] = useState('')
  const [validating, setValidating] = useState(false)
  const validatingRef = useRef(false)
  const lastScanRef = useRef<{ id: string; at: number } | null>(null)
  const { register, handleSubmit, reset } = useForm<ManualForm>()

  const loadHistory = useCallback(() => {
    validationService
      .list(eventId, 0, 8)
      .then((page) => setHistory(page.content))
      .catch(() => undefined)
  }, [eventId])

  useEffect(loadHistory, [loadHistory])

  const submitValidation = useCallback(
    async (reference: string, validationMethod: 'QR_SCAN' | 'MANUAL_ENTRY') => {
      const normalized = normalizeValidationReference(reference)
      if (!normalized || validatingRef.current) {
        return
      }

      validatingRef.current = true
      setValidating(true)
      setError('')
      try {
        const response = await validationService.validate(eventId, { id: normalized, validationMethod })
        setResult(response)
        loadHistory()
      } catch (requestError) {
        setError(getApiErrorMessage(requestError))
      } finally {
        validatingRef.current = false
        setValidating(false)
      }
    },
    [eventId, loadHistory],
  )

  const validateQr = useCallback(
    async (rawValue: string) => {
      const reference = normalizeValidationReference(rawValue)
      if (!reference) {
        return
      }

      const now = Date.now()
      const scanKey = validationReferenceKey(reference)
      if (lastScanRef.current?.id === scanKey && now - lastScanRef.current.at < 2500) {
        return
      }
      lastScanRef.current = { id: scanKey, at: now }

      await submitValidation(reference, 'QR_SCAN')
    },
    [submitValidation],
  )

  async function validateManual(values: ManualForm): Promise<void> {
    await submitValidation(values.reference, 'MANUAL_ENTRY')
    reset()
  }

  return (
    <section className="dashboard-page">
      <div className="section-header">
        <div>
          <span className="eyebrow">Staff validation</span>
          <h1>Scan tickets</h1>
        </div>
      </div>
      {error ? <ErrorState message={error} /> : null}
      <div className="scanner-grid">
        <article className="scanner-panel">
          <h2>QR scanner</h2>
          <QRScanner onScan={validateQr} />
        </article>
        <article className="scanner-panel">
          <h2>Manual entry</h2>
          <p className="field-help">{MANUAL_VALIDATION_HELP}</p>
          <form className="stack-form" onSubmit={handleSubmit(validateManual)}>
            <label>
              Ticket or QR reference
              <input
                {...register('reference', {
                  required: 'Enter a ticket ID, QR code ID, or scan payload',
                  validate: (value) =>
                    normalizeValidationReference(value).length > 0 || 'Enter a ticket ID, QR code ID, or scan payload',
                })}
                placeholder={MANUAL_VALIDATION_PLACEHOLDER}
                autoComplete="off"
                spellCheck={false}
              />
            </label>
            <button className="button primary" type="submit" disabled={validating}>
              {validating ? 'Validating' : 'Validate ticket'}
            </button>
          </form>
          {result ? (
            <div className={`validation-result validation-${result.status.toLowerCase()}`}>
              <StatusBadge status={result.status} />
              <strong>{validationMessage(result.status)}</strong>
              <span>Ticket {result.ticketId}</span>
              <small>{formatDateTime(result.validationTime)}</small>
            </div>
          ) : null}
        </article>
      </div>
      <section className="report-panel">
        <h2>Recent validations</h2>
        <div className="data-table">
          {history.map((item) => (
            <div className="table-row" key={item.id}>
              <span>{item.ticketId}</span>
              <StatusBadge status={item.status} />
              <span>{item.validationMethod}</span>
              <span>{formatDateTime(item.validationTime)}</span>
            </div>
          ))}
        </div>
      </section>
    </section>
  )
}

function validationMessage(status: TicketValidationResponse['status']): string {
  switch (status) {
    case 'SUCCESS':
      return 'Entry approved'
    case 'DUPLICATE':
      return 'Ticket already used'
    case 'INVALID':
      return 'Invalid ticket for this event'
    case 'FAILED':
      return 'Validation failed'
    default:
      return 'Validation completed'
  }
}
