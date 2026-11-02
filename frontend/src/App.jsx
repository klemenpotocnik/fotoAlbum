import { BrowserRouter, Routes, Route } from 'react-router';
import './App.css'
import Navbar from './components/Navbar'
import Home from './pages/Home'
import AlbumDetails from './pages/AlbumDetails'

function App() {

  return (
    <>
      <BrowserRouter>
        <Navbar />

        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/albums/:id" element={<AlbumDetails />} />
        </Routes>
      </BrowserRouter>
    </>
  )
}

export default App
