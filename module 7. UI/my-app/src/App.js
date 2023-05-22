import './App.css';
import {Routes, Route} from 'react-router-dom';
import {Provider} from 'react-redux';
import store from './redux/store';
import Login from './components/login/Login';
import 'bootstrap/dist/css/bootstrap.min.css';
import Header from './components/header/Header'
import Footer from './components/footer/Footer'
import Certificates from './components/certificates/Certificates';

function App() {
  return (
      <>
        <Provider store={store}>
          <Header/>
          <div className='app-content'>
            <Routes>
              <Route path='/certificates' element={<Certificates/>}></Route>
              <Route path='/login' element={<Login/>}></Route>
            </Routes>
          </div>
          <Footer/>
        </Provider>
      </>
  );
}

export default App;
