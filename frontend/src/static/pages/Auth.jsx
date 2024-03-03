import React, {Component} from "react";
import {Client} from "@stomp/stompjs";
import * as StompJs from "@stomp/stompjs";
import {Navigate, Route, useNavigate} from "react-router-dom";
import Glavniy from "./Glavniy";
import Participant from "./Participant";
import Worker from "./Worker";
import Soldier from "./Soldier";

class Auth extends Component {
    state ={
        login: '',
        password: '',
        isLogin: true,
        role: ''
    }
    handleLoginChange = (event) => {
        // 👇 Get input value from "event"
        this.setState({login : event.target.value});
    }
    handlePasswordChange = (event) => {
        // 👇 Get input value from "event"
        this.setState({password : event.target.value});
    }
    handleLoginClick = () => {
        console.log(this.state.login, this.state.password)
        const credentialsMsg=
        {
            username: this.state.login,
            password: this.state.password
        }
        fetch('http://localhost:8080/auth/login', {  // Enter your IP address here
            headers: {
                "Content-Type": "application/json"
            },
            method: 'POST',
            mode: 'cors',
            body: JSON.stringify(credentialsMsg)
            // body data type must match "Content-Type" header

        }).then(res => res.json().then(data => {
            console.log(data?.token);
            localStorage.setItem(this.state.login,data?.token)
            return this.getAccount()
        } ))

         // this.getAccount()
    }
        async getAccount () {
         console.log(localStorage.getItem(`${this.state.login}`), 'token here')
         let url = "http://localhost:8080/account/";
         url = url.concat(this.state.login)
            const tokentest= this.state.login
        fetch(url,{
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem(`${this.state.login}`)
                // "Content-Type": "application/json"
            },
            method: 'GET',
            mode: 'cors'
        }).then(res => {res.json().then(data =>{
            console.log(JSON.stringify(data))
            const headers = { Authorization: 'Bearer ' + localStorage.getItem(`${this.state.login}`)}
            if (data.role === "UNDEFINED") {
                console.log('if proshel')
                if (this.client.webSocket.readyState === WebSocket.OPEN) {
                    this.client.subscribe('/user/undefined/messages', message => {
                        const sad = JSON.parse(message.body)
                        console.log(sad.role)
                        this.setState({role: sad.role})
                    }, headers)
                }
            }
            if (data.role === "GLAVNIY") {
                console.log('glavniy')
                // this.context.router.push('../Glavniy')
                this.props.history?.push('../Glavniy')
                this.setState({role: 'GLAVNIY'})
            }
            if (data.role === "MANAGER") {
            this.setState({role: 'MANAGER'})
            }
            if (data.role === "WORKER") {
                this.setState({role: 'WORKER'})
            }
            if (data.role === "SOLDIER") {
                this.setState({role: 'SOLDIER'})
            }
            if (data.role === "PLAYER") {
                this.setState({role: 'PLAYER'})
            }
        })
    })
            this.client = new Client();
            let url2= 'ws://localhost:8080/squid-game-socket?username=';
            url2 = url2.concat(this.state.login);
            this.client.configure({
                connectHeaders: {
                    Authorization: 'Bearer ' + localStorage.getItem(`${this.state.login}`)
                },
                brokerURL: url2,
                onConnect: () => {
                    console.log('onConnect');
                    // this.handleSend();
                },
                // Helps during debugging, remove in production
                debug: (str) => {
                    console.log(new Date(), str);
                }
            });

            this.client.activate();
    }
    handleSignUpClick = () => {
        console.log(this.state.login, this.state.password)
        const credentialsMsg=
            {
                username: this.state.login,
                password: this.state.password
            }
        fetch('http://localhost:8080/auth/register', {  // Enter your IP address here
            headers: {
                "Content-Type": "application/json"
            },
            method: 'POST',
            mode: 'cors',
            body: JSON.stringify(credentialsMsg)
            // body data type must match "Content-Type" header

        }).then(() => alert("Зарегался"))
        // this.client.publish({destination: '/auth/register', body: JSON.stringify(credentialsMsg)})
    }
    showLoginBtn = () => {
        this.setState({isLogin: true})
        console.log('tut', this.state.isLogin)
    }
    showRegisterBtn = () => {
        this.setState({isLogin: false})
    }

    render() {
        const nickname = this.state.login
        return (
        <div className={"mainContainer"}>
            <div className={"titleContainer"}>
            </div>
            <br />
            <div className={"inputContainer"}>
                <input
                     value={this.state.login}
                    placeholder="Enter your email here"
                     onChange={this.handleLoginChange}
                    className={"inputBox"} />
            </div>
            <br />
            <div className={"inputContainer"}>
                <input
                     value={this.state.password}
                    placeholder="Enter your password here"
                     onChange={this.handlePasswordChange}
                    className={"inputBox"} />
            </div>
            <br />
            <div style={{display: "flex"}}>
            <div className={"inputContainer"}>
                <input
                    className={"inputButton"}
                    type="button"
                    onClick={this.handleSignUpClick}
                    value={"Sign Up"} />
            </div>

                <div className={"inputContainer"}>
                    <input
                        className={"inputButton"}
                        type="button"
                        style={{marginLeft: 15}}
                        onClick={this.handleLoginClick}
                        value={"Login"} />
                </div>
            </div>
            {this.state.role === 'GLAVNIY' &&
            <Navigate to="../Glavniy" state={this.state.login} />
            }
            {this.state.role === 'MANAGER' &&
                <Navigate to="../Manager" />
            }
            {this.state.role === 'WORKER' &&
                <Navigate to="../Worker" state={ nickname} />
            }
            {this.state.role === 'PLAYER' &&
                <Navigate to="../Participant" state={nickname}/>
            }
            {this.state.role === 'SOLDIER' &&
                <Navigate to="../Soldier" state={nickname}/>
            }
        </div>
        )
    }
}
export default Auth;