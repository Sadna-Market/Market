import logo from './logo.svg';
import './App.css';
import '../node_modules/bootstrap/dist/css/bootstrap.min.css'
import Login from './components/login'
import SignUp from './components/signUp'
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom'
import MainPage from "./components/mainPage";
import InitMarket from "./components/initMarket";

function App() {
    return (
        <Router>
            <div className="App">
                <nav className="navbar navbar-expand-lg navbar-light fixed-top">
                    <div className="container">
                        <Link className="navbar-brand" to={'/sign-in'}>
                        </Link>
                        <div className="collapse navbar-collapse" id="navbarTogglerDemo02">
                            <ul className="navbar-nav ml-auto">
                                <li className="nav-item">
                                    <Link className="nav-link" to={'/sign-in'}>
                                        Login
                                    </Link>
                                </li>
                                <li className="nav-item">
                                    <Link className="nav-link" to={'/sign-up'}>
                                        Sign up
                                    </Link>
                                </li>
                                <li className="nav-item">
                                    <Link className="nav-link" to={'/get-in'}>
                                        Visit Market
                                    </Link>
                                </li>
                                <li className="nav-item">
                                    <Link className="nav-link" to={'/init-market'}>
                                        Init market
                                    </Link>
                                </li>
                            </ul>
                        </div>
                    </div>
                </nav>
                <div className="auth-wrapper">
                    <div className="auth-inner">
                        <Routes>
                            <Route path="/sign-in" element={<Login />} />
                            <Route path="/sign-up" element={<SignUp />} />
                            <Route path="/get-in" element={<MainPage />} />
                            <Route path="/init-market" element={<InitMarket />} />

                        </Routes>
                    </div>
                </div>
            </div>
        </Router>
    )
}

export default App;
