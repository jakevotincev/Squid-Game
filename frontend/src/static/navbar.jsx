import React from "react";
import { BrowserRouter, Route, Link } from "react-router-dom";
import "./navbarstyle.css";

function Navbar() {
    return (
        <nav class="mainnavig">
            <ul>
                <li>
                    <Link to="/">Home</Link>
                </li>
                <li>
                    <Link to="/Glavniy">Glavniy</Link>
                </li>
                <li>
                    <Link to="/Manager">Manager</Link>
                </li>
                <li>
                    <Link to="/Participant">Participant</Link>
                </li>
                <li>
                    <Link to="/Worker">Worker</Link>
                </li>
                <li>
                    <Link to="/Soldier">Soldier</Link>
                </li>
                <li>
                    <Link to="/Auth">Auth</Link>
                </li>
            </ul>
        </nav>
    );
}

export default Navbar;