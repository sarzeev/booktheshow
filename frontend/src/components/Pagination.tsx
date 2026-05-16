interface PaginationProps {
  page: number
  totalPages: number
  onPageChange: (page: number) => void
}

export function Pagination({ page, totalPages, onPageChange }: PaginationProps): React.JSX.Element | null {
  if (totalPages <= 1) {
    return null
  }

  return (
    <nav className="pagination" aria-label="Pagination">
      <button className="button ghost" type="button" disabled={page === 0} onClick={() => onPageChange(page - 1)}>
        Previous
      </button>
      <span>
        Page {page + 1} of {totalPages}
      </span>
      <button
        className="button ghost"
        type="button"
        disabled={page + 1 >= totalPages}
        onClick={() => onPageChange(page + 1)}
      >
        Next
      </button>
    </nav>
  )
}
