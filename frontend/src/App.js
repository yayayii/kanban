import React from 'react';
import './App.css';
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import Home from './Home';
import AppNavbar from "./AppNavbar";
import TaskCreate from "./TaskCreate";
import TaskView from "./TaskView";
import TaskEdit from "./TaskEdit";

const App = () => {
    return (
        <Router>
        <AppNavbar/>
            <Routes>
                <Route path='/' exact={true} element={<Home/>}/>
                <Route path='/create' exact={true} element={<TaskCreate/>}/>
                <Route path='/view/:id' element={<TaskView/>}/>
                <Route path='/edit/:id' element={<TaskEdit/>}/>
            </Routes>
        </Router>
    )
}

export default App;
