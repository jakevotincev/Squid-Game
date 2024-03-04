import React, { Component } from 'react';
import {Client} from "@stomp/stompjs";
import Popup from 'reactjs-popup';
import 'reactjs-popup/dist/index.css';
import "./pagestyle.css";
import {useLocation, useNavigate, useParams} from "react-router-dom";
import Clicker from "./Clicker";

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
        gameId: null,
        showPlayersAnketas: true,
        workerId: null,
        showQuiz: false,
        showMath: false,
        cleaningScore: 0,
        showClicker: false
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
        let url = "http://localhost:8080/account/";
        url = url.concat(this.props.location.state)
        fetch(url,{
            headers: {
                // "Content-Type": "application/json"
                'Authorization': 'Bearer ' + localStorage.getItem(`${this.props.location.state}`)
            },
            method: 'GET',
            mode: 'cors'
        }).then(
            res => {res.json().then(data =>{

                this.setState({workerId: data.id})
                console.log(JSON.stringify(data))
            })
            }

        )
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

        }).then()
        this.setState({showPlayersAnketas: false})
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
                    li.id = 'anketa';
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
                const lunchMsg = JSON.parse(message.body)
                if (lunchMsg.type === 'LUNCH_STARTED') {
                    this.setState({showQuiz: true})
                    let answersCounter = 0
                    let url = 'http://localhost:8080/account/'
                    url = url.concat(this.state.workerId)
                    url = url.concat('/questions')
                    console.log(url, 'url')
                    fetch(url,{
                        headers: {
                            "Content-Type": "application/json",
                            'Authorization': 'Bearer ' + localStorage.getItem(`${this.props.location.state}`)
                        },
                        method: 'GET',
                        mode: 'cors'
                    }) .then(res => {res.json().then(data =>{
                        console.log(JSON.stringify(data))
                        const questionsData = data
                        let quizArea = document.getElementById('quiz');
                        let counter = questionsData.length
                        let isAnswerCorrect;
                        for (let i=0; i<counter;i++) {
                            let curr_id = questionsData[i].id;
                            let curr_question = questionsData[i].question;
                            let curr_answers = questionsData[i].answers;
                            let curr_otvet;
                            let str = "Вопрос №"+i+".  "+curr_question;
                            let emptyStr = document.createElement('br');
                            let text = document.createTextNode(str);
                            let radioBtnDiv = document.createElement('div');
                            let checkAnswerBtn = document.createElement('button');
                            checkAnswerBtn.type="submit";
                            checkAnswerBtn.innerHTML="Отправить ответ";
                            console.log("curr_answers",curr_answers);
                            for (let i=0; i<curr_answers.length;i++) {
                                let label = document.createElement("label");
                                label.innerText = curr_answers[i];
                                let input = document.createElement("input");
                                input.type = "radio";
                                input.name = "colour";
                                input.value = curr_answers[i];
                                const radioButtons = document.querySelectorAll('input[name="colour"]');
                                input.addEventListener('click', (event) => {
                                    if (event.currentTarget.checked){
                                        curr_otvet = curr_answers[i];
                                    }
                                });

                                label.appendChild(input)
                                radioBtnDiv.appendChild(label)
                                radioBtnDiv.appendChild(emptyStr)
                                radioBtnDiv.appendChild(emptyStr)
                                radioBtnDiv.appendChild(checkAnswerBtn)

                            }
                            quizArea.appendChild(text)
                            quizArea.appendChild(emptyStr)
                            quizArea.appendChild(radioBtnDiv)
                            quizArea.appendChild(emptyStr)
                            // if (answersCounter < 3) {
                                checkAnswerBtn.addEventListener('click', (ev) => {
                                    // answersCounter +=1
                                    console.log('curr_otvet', curr_otvet);
                                    let destination = "http://localhost:8080/checkAnswer/";
                                    destination = destination.concat(this.state.workerId);
                                    destination = destination.concat('/');
                                    let quest_id = curr_id.toString();
                                    quest_id = quest_id.concat('/');
                                    destination = destination.concat(quest_id);
                                    destination = destination.concat(curr_otvet);
                                    console.log("dist", destination);

                                    fetch(destination, {
                                        headers: {
                                            // "Content-Type": "application/json"
                                            'Authorization': 'Bearer ' + localStorage.getItem(`${this.props.location.state}`)
                                        },
                                        method: 'GET',
                                        mode: 'cors'
                                    }).then(
                                        res => res.text().then(resData => {
                                            answersCounter = answersCounter + 1
                                            isAnswerCorrect = resData;
                                            console.log(isAnswerCorrect);
                                            if (isAnswerCorrect === 'true') {
                                                console.log('answer true')

                                            } else if (isAnswerCorrect === 'false') {
                                                console.log('answer false')

                                            }
                                            console.log(answersCounter,'counter')
                                            if (answersCounter === 3) {
                                                console.log('tuta')
                                                    this.setState({showQuiz: false})
                                                    let urlReady = 'http://localhost:8080/account/'
                                                    urlReady = urlReady.concat(this.state.workerId)
                                                    urlReady = urlReady.concat('/readyToGame')
                                                    fetch(urlReady,{
                                                        headers: {
                                                            // "Content-Type": "application/json"
                                                            'Authorization': 'Bearer ' + localStorage.getItem(`${this.props.location.state}`)
                                                        },
                                                        method: 'GET',
                                                        mode: 'cors'
                                                    })
                                            }
                                        })
                                    )
                                })
                            // } else {
                            //     console.log('tuta')
                            //     this.setState({showQuiz: false})
                            //     let urlReady = 'http://localhost:8080/account/'
                            //     urlReady = urlReady.concat(this.state.workerId)
                            //     urlReady = urlReady.concat('/readyToGame')
                            //     fetch({
                            //         headers: {
                            //             // "Content-Type": "application/json"
                            //             'Authorization': 'Bearer ' + localStorage.getItem(`${this.props.location.state}`)
                            //         },
                            //         method: 'GET',
                            //         mode: 'cors'
                            //     })
                            // }
                        }

                    })
                    })

                }
                if (lunchMsg.type === 'START_ROUND_PREPARING') {
                    this.setState({showMath: true})
                    let answersCounter = 0
                    let url = 'http://localhost:8080/account/'
                    url = url.concat(this.state.workerId)
                    url = url.concat('/questions')
                    console.log(url, 'url')
                    fetch(url, {
                        headers: {
                            "Content-Type": "application/json",
                            'Authorization': 'Bearer ' + localStorage.getItem(`${this.props.location.state}`)
                        },
                        method: 'GET',
                        mode: 'cors'
                    }).then(res => {
                        res.json().then(data => {
                            console.log(JSON.stringify(data))
                            const questionsData = data
                            let mathArea = document.getElementById('math');
                            let counter = questionsData.length
                            let isAnswerCorrect;
                            for (let i = 0; i < counter; i++) {
                                let curr_id = questionsData[i].id;
                                let curr_question = questionsData[i].question;
                                let curr_otvet;
                                let str = "Вопрос №" + i + ".  " + curr_question;
                                console.log(str, 'str')
                                let emptyStr = document.createElement('br');
                                let text = document.createTextNode(str);
                                let input = document.createElement('input')
                                let checkAnswerBtn = document.createElement('button');
                                checkAnswerBtn.type = "submit";
                                checkAnswerBtn.innerHTML = "Отправить ответ";
                                input.addEventListener('change', (event) => {
                                    // if (event.currentTarget.checked){
                                    curr_otvet = event.target.value
                                    // }
                                })
                                mathArea.appendChild(text)
                                mathArea.appendChild(input)
                                mathArea.appendChild(checkAnswerBtn)
                                mathArea.appendChild(emptyStr)
                                mathArea.appendChild(emptyStr)
                                checkAnswerBtn.addEventListener('click', (ev) => {
                                    // answersCounter +=1
                                    console.log('curr_otvet', curr_otvet);
                                    let destination = "http://localhost:8080/checkAnswer/";
                                    destination = destination.concat(this.state.workerId);
                                    destination = destination.concat('/');
                                    let quest_id = curr_id.toString();
                                    quest_id = quest_id.concat('/');
                                    destination = destination.concat(quest_id);
                                    destination = destination.concat(curr_otvet);
                                    console.log("dist", destination);

                                    fetch(destination, {
                                        headers: {
                                            // "Content-Type": "application/json"
                                            'Authorization': 'Bearer ' + localStorage.getItem(`${this.props.location.state}`)
                                        },
                                        method: 'GET',
                                        mode: 'cors'
                                    }).then(
                                        res => res.text().then(resData => {
                                            answersCounter = answersCounter + 1
                                            isAnswerCorrect = resData;
                                            console.log(isAnswerCorrect);
                                            if (isAnswerCorrect === 'true') {
                                                console.log('answer true')

                                            } else if (isAnswerCorrect === 'false') {
                                                console.log('answer false')

                                            }
                                            console.log(answersCounter, 'counter')
                                        })
                                    )
                                })
                            }
                        })
                    })
                }
                if (lunchMsg.type === 'ROUND_PREPARING_COMPLETED') {
                    this.setState({showMath: false})
                }
                if (lunchMsg.type === 'START_CLEANING') {
                    this.setState({showClicker: true})
                }
                if (lunchMsg.type === 'CLEANING_COMPLETED') {
                    this.setState({showClicker: true})
                }

            }, headers)

        } else {
            // Queue a retry
            setTimeout(() => { this.handleSend() }, 100)
        }
    }
    addPoint = () => {
        this.setState({cleaningScore: this.state.cleaningScore + 1})
        if (this.state.cleaningScore !== 0 && this.state.cleaningScore % 5 === 0) {
            let url = 'http://localhost:8080/worker/'
            url = url.concat(this.state.workerId)
            url = url.concat('/score')
            const workerScoreMsg = {
                score: this.state.cleaningScore
            }
            fetch(url,{
                headers: {
                    "Content-Type": "application/json",

                    'Authorization': 'Bearer ' + localStorage.getItem(`${this.props.location.state}`)
                },
                method: 'POST',
                mode: 'cors',
                body: JSON.stringify(workerScoreMsg)
            })
        }
    }

    render(){return(
        <div class="participant" align="center">
            <h1 id="page_title">This is worker page</h1>
            {/*<button id="connect" className="btn btn-default" type="submit" onClick={this.componentDidMount}>Connect</button>*/}
            {/*<h3>Пожалуйста, введите свой никнейм </h3>*/}
            {/*<label>Никнейм: </label>*/}
            {/*<input type="text" id="nick" name="nick" placeholder="Введите ваш никнейм" onChange={this.handleChange}/>*/}
            <br/>
            {this.state.showPlayersAnketas === true &&
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
                <br/>
                <button type="submit" onClick={this.handleFinalClick}>Завершить отбор анкет</button>
            </div>}
            {this.state.showQuiz === true &&
            <div id='quiz'></div>}
            {this.state.showMath === true &&
                <div id='math'></div>}
            {this.state.showClicker === true &&
            <div>
                <Clicker
                    points={this.state.cleaningScore}
                    onClick={this.addPoint}
                    name={'Убрано органов : '}/>
            </div>}
        </div>
    );
}}
export default withRouter(Worker);