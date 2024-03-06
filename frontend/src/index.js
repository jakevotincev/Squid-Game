import React ,{ useState, useEffect } from 'react';
import ReactDOM from 'react-dom/client';
// import io from 'socket.io-client';

import { BrowserRouter, Routes, Route } from "react-router-dom";
import './index.css';
import Glavniy from './static/pages/Glavniy';
import Manager from "./static/pages/manager";
import Layout from "./static/pages/layout";
import Participant from "./static/pages/Participant";
import $ from 'https://code.jquery.com/jquery-3.7.0.js';
import SockJS from 'https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.4.0/sockjs.min.js';
import Stomp from 'https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js';
import Worker from "./static/pages/Worker";
import Soldier from "./static/pages/Soldier";
import Auth from "./static/pages/Auth";

export default function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Layout />}>
                    <Route path="glavniy" element={<Glavniy />} />
                    <Route path="manager" element={<Manager />} />
                    <Route path="Participant" element={<Participant />} />
                    <Route path="Worker" element={<Worker />} />
                    <Route path="Soldier" element={<Soldier />} />
                    <Route path="Auth" element={<Auth />} />
                </Route>
            </Routes>
        </BrowserRouter>
    );
}

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  // <React.StrictMode>
    <App />
  // </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
