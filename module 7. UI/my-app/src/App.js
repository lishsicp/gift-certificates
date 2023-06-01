import { Routes, Route, useNavigate } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import Header from "./components/header/Header";
import Footer from "./components/footer/Footer";
import Certificates from "./components/certificates/Certificates";
import {useEffect} from "react";

function App() {
  const navigate = useNavigate()

  useEffect(() => {
    if (window.location.pathname === '/') {
      navigate('/certificates');
    }
  }, [navigate]);

  return (
    <>
      <Header />
      <Routes>
        <Route path="/certificates" element={<Certificates />}>
        </Route>
      </Routes>
      <Footer />
    </>
  );
}

export default App;
