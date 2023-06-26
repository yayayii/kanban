import React from 'react';
import './App.css';
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import Home from './Home';
import AppNavbar from "./AppNavbar";

const App = () => {
  return (
      <Router>
        <AppNavbar/>
        <Routes>
          <Route path='/' exact={true} element={<Home/>}/>
        </Routes>
      </Router>
  )
}

export default App;
