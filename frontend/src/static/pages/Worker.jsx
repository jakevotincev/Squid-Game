import React, { Component } from 'react';
import {Client} from "@stomp/stompjs";
import Popup from 'reactjs-popup';
import 'reactjs-popup/dist/index.css';
import "./pagestyle.css";
import {useLocation, useNavigate, useParams} from "react-router-dom";

export function withRouter(Children) {
    return(props)=>{
        const location = useLocation()
        return <Children {...props} location = {location}/>
    }
}

class Worker extends Component{
    state = {
        nickname: "",
        acceptedForms: null,
        criteria: "",
        playersNumber: null,
        anketa_Id: null,
        anketa_content: null,
        msgRecieved: true,
        anketa: [{}],
        gameId: null
    }
    handleChange = (event) => {
        // 👇 Get input value from "event"
        this.setState({nickname : event.target.value});

    }
    getNickname() {
        this.setState({nickname: this.props.location.state})
        let url= 'ws://localhost:8080/squid-game-socket?username=';
        url = url.concat(this.props.location.state);
        return url
    }
    componentDidMount = () => {
        if (this.props.location.state !== ""){
            console.log('Component did mount');
            console.log(this.state.nickname);
            // The compat mode syntax is totally different, converting to v5 syntax
            // Client is imported from '@stomp/stompjs'
            this.client = new Client();

            this.client.configure({
                brokerURL: this.getNickname(),
                connectHeaders: {
                    Authorization: 'Bearer ' + localStorage.getItem(`${this.props.location.state}`)
                },
                onConnect: () => {
                    console.log('onConnect');
                    this.handleSend();
                },
                // Helps during debugging, remove in production
                debug: (str) => {
                    console.log(new Date(), str);
                }
            });

            this.client.activate();
        }

    }
    handleFinalClick = (event) =>{
        console.log(this.state.anketa);
         let acceptedformsCount = this.state.acceptedForms;
           let criteria = {playersNumber: this.state.playersNumber, criteria: this.state.criteria, gameId: this.state.gameId};
        let forms = this.state.anketa;
        console.log(acceptedformsCount);
        console.log(criteria);
        console.log(forms);
        let datta = {acceptedFormsCount: this.state.acceptedForms, criteria: {playersNumber: this.state.playersNumber, criteria: this.state.criteria, gameId: this.state.gameId}, forms: this.state.anketa};
        console.log(datta);
        fetch('http://localhost:8080/acceptForms', {  // Enter your IP address here
            headers: {
                "Content-Type": "application/json",
                'Authorization': 'Bearer ' + localStorage.getItem(`${this.props.location.state}`)
            },
            method: 'POST',
            mode: 'cors',
            body: JSON.stringify(datta)
            // body data type must match "Content-Type" header

        }).then(() => alert("работу кончил"))
    }

    // getFromInputz(inputName){
    //     return document.getElementById(inputName).value;
    // }

    handleSend = () => {
        if (this.client.webSocket.readyState === WebSocket.OPEN) {
            const headers = { Authorization: 'Bearer ' + localStorage.getItem(`${this.props.location.state}`)}
            this.client.subscribe('/user/worker/messages', message => {

                console.log(JSON.parse(message.body));
                let formsMsg = JSON.parse(message.body);
                console.log(formsMsg);
                // this.setState({acceptedForms: formsMsg.acceptedFormsCount});
                this.state.acceptedForms = formsMsg.acceptedFormsCount;
                console.log(this.state.acceptedForms);
                this.setState({playersNumber: formsMsg.criteria.playersNumber});
                this.setState({criteria: formsMsg.criteria.criteria});
                this.setState({formsNumber: formsMsg.forms.length});
                this.setState({gameId: formsMsg.criteria.gameId});
                let anketaArea = document.getElementById('anketas_list');
                let allAnketas = formsMsg.forms;
                let a = [];
                for (let i=0; i < formsMsg.forms.length; i++){
                    let curr_anketa = formsMsg.forms[i];
                    this.setState({anketa_Id: formsMsg.forms[i].playerId});
                    this.setState({anketa_content: formsMsg.forms[i].content});
                    let li = document.createElement("li");
                    let yesCheckBox = document.createElement('input');
                    let yesLabel = document.createElement('label');
                    let emptyStr = document.createElement('br');
                    yesLabel.innerHTML = "    Принять анкету";
                    yesCheckBox.type="checkbox";
                    yesCheckBox.value="Yes";
                    yesCheckBox.checked=false;
                    console.log("anketka",curr_anketa);
                    yesCheckBox.addEventListener('change',ev => {
                        if(ev.currentTarget.checked){
                            // this.state.anketa = this.state.anketa.push(curr_anketa);
                            a.push(curr_anketa);

                             console.log(a);
                             this.setState({anketa: a});
                        }
                    })
                    // yesCheckBox.onchange((event)=> {
                    //     this.handleClick();
                    //     this.state.anketa = this.state.anketa.push(curr_anketa);
                    // })
                    let id = formsMsg.forms[i].playerId;
                    let content = formsMsg.forms[i].content;
                    let str = "Id игрока : "+ id +", Анкета игрока : "+ content+ "     ";
                    li.appendChild(document.createTextNode(str));
                    li.appendChild(emptyStr);
                    li.appendChild(yesLabel);
                    li.appendChild(yesCheckBox);
                    // li.appendChild(noLabel);
                    // li.appendChild(noCheckBox);
                    anketaArea.appendChild(li);
                    console.log(curr_anketa);

                }
                return allAnketas
            }, headers)
            this.client.subscribe('/worker/messages', message => {
                console.log('govnomapping ',JSON.parse(message.body));
            }, headers)

        } else {
            // Queue a retry
            setTimeout(() => { this.handleSend() }, 100)
        }
    };

    render(){return(
        <div class="participant" align="center">
            <h1 id="page_title">This is worker page</h1>
            {/*<button id="connect" className="btn btn-default" type="submit" onClick={this.componentDidMount}>Connect</button>*/}
            {/*<h3>Пожалуйста, введите свой никнейм </h3>*/}
            {/*<label>Никнейм: </label>*/}
            {/*<input type="text" id="nick" name="nick" placeholder="Введите ваш никнейм" onChange={this.handleChange}/>*/}
            <br/>

            <div id="worker_interface">
                <div id="conditions" >
                    <p>
                        Количество анкет, которые необходимо принять : {this.state.acceptedForms}
                    </p>
                    <p>
                        Критерии отбора :
                    </p>
                    <p>
                        Предложенное количество участников : {this.state.playersNumber},  Критерии отбора : {this.state.criteria}
                    </p>
                </div>

                <div id="anketas" >
                    <ul id="anketas_list">

                    </ul>

                </div>

            </div>

            <button type="submit" onClick={this.handleFinalClick}>Завершить отбор анкет</button>
        </div>
    );
}}
export default withRouter(Worker);