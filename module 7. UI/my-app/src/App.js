
import './App.css';
import { Routes, Route } from 'react-router-dom';
import { Provider } from 'react-redux';
import store from './redux/store';
import Login from './login/login.js';
import 'bootstrap/dist/css/bootstrap.min.css';
import Header from './header/header.js'
import Footer from './footer/footer.js'
import Certificates from './certificates/certificates';

function App() {
  return (
    <>
      <Provider store={store}>
      <Header />
        <div className='app-content'>
          <Routes>
            <Route path='/' element={<Certificates />}></Route>
            <Route path='/login' element={<Login />}></Route>
          </Routes>
        </div>
        <Footer />
      </Provider>
      </>
  );
}

export default App;
