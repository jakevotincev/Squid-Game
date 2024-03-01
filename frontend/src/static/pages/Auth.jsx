import React, {Component} from "react";

class Auth extends Component {
    state ={
        login: '',
        password: '',
        isLogin: true
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

        }).then(res => res.json().then(data => {console.log(data?.token); localStorage.setItem('token',data?.token)} ))

        // this.client.publish({destination: '/auth/login', body: JSON.stringify(credentialsMsg)})
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
        return (
        <div className={"mainContainer"}>
            <div className={"titleContainer"}>
                <div>
                    <input
                    className={"inputButton"}
                    type="button"
                    onClick={this.showRegisterBtn}
                    value={"Register"} />
                </div>
                <div>
                    <input
                    className={"inputButton"}
                    type="button"
                    style={{marginLeft: 15}}
                    onClick={this.showLoginBtn}
                    value={"Login"} />
            </div>
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
            {this.isLogin &&
            <div className={"inputContainer"}>
                <input
                    className={"inputButton"}
                    type="button"
                    onClick={this.handleLoginClick}
                    value={"Log in"} />
            </div>}
            {!this.isLogin &&
                <div className={"inputContainer"}>
                    <input
                        className={"inputButton"}
                        type="button"
                        onClick={this.handleLoginClick}
                        value={"Sign Up"} />
                </div>}
        </div>
        )
    }
}
export default Auth;