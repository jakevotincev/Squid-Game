const url = "http://localhost:8080/account/";

class AccountService {
    getAccountId(nickname) {
        return fetch(url.concat(nickname), {
            method: 'GET',
            mode: 'cors'
        }).then(
            res => {
                return res.json();
            }
        )
    }
}

export default new AccountService();