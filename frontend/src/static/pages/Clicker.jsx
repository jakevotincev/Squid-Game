
const Clicker = ({points, onClick, name}) => {
    return (
        <div><button className="ClickerBtn"
                     onClick={onClick}>Работаем!</button>
            <br/>
            <h4>{name} {points}</h4>
            <br/>

        </div>
    )
}
export default Clicker