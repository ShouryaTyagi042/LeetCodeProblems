import { Navigate, Route, Routes } from 'react-router-dom'
import Shell from './components/Shell'
import ProblemsPage from './pages/ProblemsPage'
import ProblemPage from './pages/ProblemPage'
import NewProblemPage from './pages/NewProblemPage'
import ReviewPage from './pages/ReviewPage'

export default function App() {
  return (
    <Routes>
      <Route element={<Shell />}>
        <Route path="/" element={<Navigate to="/problems" replace />} />
        <Route path="/problems" element={<ProblemsPage />} />
        <Route path="/review" element={<ReviewPage />} />
        <Route path="/problems/new" element={<NewProblemPage />} />
        <Route path="/problems/:topic/:name" element={<ProblemPage />} />
      </Route>
    </Routes>
  )
}
