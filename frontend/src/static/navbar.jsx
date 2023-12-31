import React from "react";
import { BrowserRouter, Route, Link } from "react-router-dom";

function Navbar() {
    return (
        <nav>
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
            </ul>
        </nav>
    );
}

export default Navbar;