import {Routes, Route} from 'react-router-dom';
import Login from './components/login/Login';
import 'bootstrap/dist/css/bootstrap.min.css';
import Header from './components/header/Header'
import Footer from './components/footer/Footer'
import Certificates from './components/certificates/Certificates';

function App() {
  return (
      <>
          <Header/>
          <div className='app-content'>
            <Routes>
              <Route path='/certificates' element={<Certificates/>}></Route>
              <Route path='/login' element={<Login/>}></Route>
            </Routes>
          </div>
          <Footer/>
      </>
  );
}

export default App;
