import { useState } from 'react'
import { useForm } from 'react-hook-form'
import { Link, useNavigate } from 'react-router-dom'
import { getApiErrorMessage } from '../../api/client'
import type { RegisterRequest, Role } from '../../api/types'
import { ErrorState } from '../../components/ErrorState'
import { useAuth } from '../../hooks/useAuth'

type RegisterForm = Omit<RegisterRequest, 'roles'> & { role: Role }

export function RegisterPage(): React.JSX.Element {
  const { register: registerAccount } = useAuth()
  const navigate = useNavigate()
  const [error, setError] = useState('')
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<RegisterForm>({ defaultValues: { role: 'ROLE_ATTENDEE' } })

  async function submit(values: RegisterForm): Promise<void> {
    setError('')
    try {
      await registerAccount({ ...values, roles: [values.role] })
      navigate(values.role === 'ROLE_ORGANIZER' ? '/organizer' : values.role === 'ROLE_STAFF' ? '/staff' : '/attendee')
    } catch (requestError) {
      setError(getApiErrorMessage(requestError))
    }
  }

  return (
    <section className="auth-page">
      <div className="auth-panel wide">
        <span className="eyebrow">New account</span>
        <h1>Register for BookTheShow</h1>
        <p>Choose the role that matches your workflow: attendee, organizer, or staff.</p>
        {error ? <ErrorState message={error} /> : null}
        <form className="form-grid" onSubmit={handleSubmit(submit)}>
          <label>
            First name
            <input {...register('firstName', { required: 'First name is required' })} />
            {errors.firstName ? <span className="field-error">{errors.firstName.message}</span> : null}
          </label>
          <label>
            Last name
            <input {...register('lastName', { required: 'Last name is required' })} />
            {errors.lastName ? <span className="field-error">{errors.lastName.message}</span> : null}
          </label>
          <label>
            Email
            <input type="email" {...register('email', { required: 'Email is required' })} />
            {errors.email ? <span className="field-error">{errors.email.message}</span> : null}
          </label>
          <label>
            Password
            <input type="password" {...register('password', { required: 'Password is required', minLength: 8 })} />
            {errors.password ? <span className="field-error">Password must be at least 8 characters</span> : null}
          </label>
          <label className="span-2">
            Role
            <select {...register('role')}>
              <option value="ROLE_ATTENDEE">Attendee</option>
              <option value="ROLE_ORGANIZER">Organizer</option>
              <option value="ROLE_STAFF">Staff</option>
            </select>
          </label>
          <button className="button primary span-2" type="submit" disabled={isSubmitting}>
            {isSubmitting ? 'Creating account' : 'Register'}
          </button>
        </form>
        <Link to="/login">Already have an account?</Link>
      </div>
    </section>
  )
}
