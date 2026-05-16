import { useState } from 'react'
import { useForm } from 'react-hook-form'
import { Link, useLocation, useNavigate } from 'react-router-dom'
import { getApiErrorMessage } from '../../api/client'
import type { LoginRequest } from '../../api/types'
import { ErrorState } from '../../components/ErrorState'
import { useAuth } from '../../hooks/useAuth'

export function LoginPage(): React.JSX.Element {
  const { login } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()
  const [error, setError] = useState('')
  const from = (location.state as { from?: { pathname?: string } } | null)?.from?.pathname ?? '/'
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<LoginRequest>()

  async function submit(values: LoginRequest): Promise<void> {
    setError('')
    try {
      await login(values)
      navigate(from, { replace: true })
    } catch (requestError) {
      setError(getApiErrorMessage(requestError))
    }
  }

  return (
    <section className="auth-page">
      <div className="auth-panel">
        <span className="eyebrow">Welcome back</span>
        <h1>Log in to BookTheShow</h1>
        <p>Access tickets, organizer tools, and staff validation with your JWT-backed account.</p>
        {error ? <ErrorState message={error} /> : null}
        <form className="stack-form" onSubmit={handleSubmit(submit)}>
          <label>
            Email
            <input type="email" {...register('email', { required: 'Email is required' })} />
            {errors.email ? <span className="field-error">{errors.email.message}</span> : null}
          </label>
          <label>
            Password
            <input type="password" {...register('password', { required: 'Password is required' })} />
            {errors.password ? <span className="field-error">{errors.password.message}</span> : null}
          </label>
          <button className="button primary" type="submit" disabled={isSubmitting}>
            {isSubmitting ? 'Logging in' : 'Login'}
          </button>
        </form>
        <Link to="/register">Create an account</Link>
      </div>
    </section>
  )
}
