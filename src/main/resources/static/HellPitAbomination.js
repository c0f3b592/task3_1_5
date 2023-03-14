async function getUsersJson() {
    let response = await fetch('http://localhost:8077/admin/getUsers');
    if (response.ok) {
        return await response.json();
    } else {
        alert("Error HTTP: " + response.status);
    }
}

async function getUserByIDJson(id) {
    let response = await fetch('http://localhost:8077/admin/getUserByID/' + id);
    if (response.ok) {
        return await response.json();
    } else {
        alert("Error HTTP: " + response.status);
    }
}

async function getAllRolesJson() {
    let response = await fetch('http://localhost:8077/admin/getRoles');
    if (response.ok) {
        return await response.json();
    } else {
        alert("Error HTTP: " + response.status);
    }
}

async function setNavBarContent() {
    let nabBar = document.getElementById('navbar-content');
    fetch('http://localhost:8077/user/getAuthUser')
        .then(response => response.json())
        .then(json => {
            let username = json['username'];
            let rolesString = '';
            for (let role of json['roles']) {
                rolesString += role.role + ' ';
            }
            nabBar.innerHTML = '<p class="navbar-text ms-0 my-0 px-0 text-light"> ' +
                                username +  ' with roles: ' + rolesString + '</p>' +
                                '<a class="nav-link me-0 text-light" href="/logout">logout</a>'
        })
}

async function addUser() {
    getAllRolesJson().then(roles => {
        let userID = null;
        let username = document.getElementById('new-user-username').value;
        let password = document.getElementById('new-user-password').value;
        let enabled = true;
        let isADMIN = document.getElementById('new-user-ADMIN').selected;
        let roleADMIN = false;
        if (isADMIN) {
            roleADMIN = true;
        }
        let isUSER = document.getElementById('new-user-USER').selected;
        let roleUSER = false;
        if (isUSER) {
            roleUSER = true;
        }
        let rolesList = [];
        for (let role of roles) {
            if (role['role'] === 'ADMIN' && roleADMIN) {
                rolesList.push(role);
            }
            if (role['role'] === 'USER' && roleUSER) {
                rolesList.push(role);
            }
        }

        let jsonBody = JSON.stringify({
            id: userID,
            username: username,
            password: password,
            enabled: enabled,
            roles: rolesList
        })
        fetch('http://localhost:8077/admin', {
            method: 'POST',
            body: jsonBody,
            headers: {
                'Content-type': 'application/json; charset=UTF-8',
            },
        })
    });
    let triggerEl = document.querySelector('#list-tab');
    (new bootstrap.Tab(triggerEl)).show(); // Select tab by name
    await sleep(500);
    updateUsersTable();
}

function setAddListener() {
    let addButton = document.getElementById('submit-new-user');
    addButton.addEventListener('click', addUser);
}

function updateUsersTable() {
    getUsersJson().then(users => {
        const keys = Object.keys(users[0]);
        let tableData = '<thead><tr>';
        for (let key of keys) {
            tableData += '<th><strong>' + key + '</strong></th>';
        }

        tableData += '<th><strong>edit</strong></th>';
        tableData += '<th><strong>delete</strong></th>';

        tableData += '</tr></thead>' + '<tbody>';
        for (let user of users) {
            tableData += '<tr id="user-' + user['id'] + '">';

            for (let key of keys) {
                tableData += '<td id="' + key + '-' + user['id'] + '" class="user-data">';
                if (key === 'roles') {
                    let roles = user[key];
                    for (let role of roles) {
                        tableData += role['role'] + ' ';
                    }
                } else {
                    tableData += user[key];
                }
                tableData += '</td>'
            }
            tableData += '<td><button id="edit-' + user['id'] + '" type="button" class="edit btn btn-primary" data-bs-toggle="modal" data-bs-target="#modal" data-bs-whatever="edit">edit</button></td>';
            tableData += '<td><button id="delete-' + user['id'] + '" type="button" class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#modal" data-bs-whatever="delete"><strong>delete</strong></button></td>';
            tableData += '</tr>';
        }
        tableData += '</tbody>';
        document.getElementById('list-users').innerHTML = tableData;
    });
}

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

async function setModalListener() {
    await sleep(500);
    let modal = await document.getElementById('modal');
    modal.addEventListener('show.bs.modal', modalFiller);
    modal.addEventListener('hidden.bs.modal', modalClose);
}

function modalFiller(event) {
    let button = event.relatedTarget;
    let recipient = button.getAttribute('id');
    let actionIdList = recipient.split('-');
    let action = actionIdList[0];
    let id = actionIdList[1];

    getUserByIDJson(id).then(user => {
        const keys = Object.keys(user);
        let modalBody = document.querySelector('.modal-body');
        let submitButtonClass = document.querySelector('#submit-button');
        submitButtonClass.innerHTML = 'edit';
        submitButtonClass.setAttribute('class', 'btn btn-primary')

        if (action === 'edit') {
            let modalTitle = document.querySelector('.modal-title');
            modalTitle.innerHTML = 'edit user';
            let modalBodyContent = '';
            modalBodyContent += '<h6 class="text-center"><strong>id</strong></h6>\n' +
                '                <div class="input-group">\n' +
                '                    <input id="user-id" type="text" class="form-control" disabled>\n' +
                '                </div>\n' +
                '                <h6 class="text-center pt-3"><strong>username</strong></h6>\n' +
                '                <div class="input-group">\n' +
                '                    <input id="user-username" type="text" class="form-control" required>\n' +
                '                </div>\n' +
                '                <h6 id="password-h" class="text-center pt-3"><strong>password</strong></h6>\n' +
                '                <div class="input-group">\n' +
                '                    <input id="user-password" type="text" class="form-control" required>\n' +
                '                </div>\n' +
                '                <div class="form-check form-switch ps-0 pt-3 d-flex justify-content-center">\n' +
                '                    <input id="user-enabled" class="form-check-input" type="checkbox">\n' +
                '                        <label id="enabled-label" class="form-check-label ps-2"\n' +
                '                               for="user-enabled"><strong>enabled</strong></label>\n' +
                '                </div>\n' +
                '                <h6 class="text-center pt-3"><strong>roles</strong></h6>\n' +
                '                <select id="user-roles" class = "form-select" size = "2" multiple >\n' +
                '                </select>'

            modalBody.innerHTML = modalBodyContent;
            for (let key of keys) {
                if (key === 'roles') {
                    let rolesContent = '';
                    getAllRolesJson()
                        .then(roles => {
                            for (let role of roles) {
                                rolesContent += '<option id="role-' + role.role + '" label="' + role.role + '"> </option>'
                            }
                            modalBody.querySelector('#user-' + key).innerHTML = rolesContent;
                            for (let role of user[key]) {
                                let options = modalBody.querySelector('#role-' + role.role);
                                if (options) {
                                    options.setAttribute('selected', 'selected');
                                }
                            }
                        })
                } else if (key === 'enabled') {
                    if (user[key] === true) {
                        modalBody.querySelector('#user-' + key).setAttribute('checked', 'true');
                    }
                } else {
                    modalBody.querySelector('#user-' + key).setAttribute('value', user[key]);
                }
            }

        } else if (action === 'delete') {
            let modalTitle = document.querySelector('.modal-title');
            modalTitle.innerHTML = 'delete user';
            let submitButtonClass = document.querySelector('#submit-button');
            submitButtonClass.innerHTML = 'delete';
            submitButtonClass.setAttribute('class', 'btn btn-danger')

            let modalBodyContent = '';
            modalBodyContent += '<h6 class="text-center"><strong>id</strong></h6>\n' +
                '                <div class="input-group">\n' +
                '                    <input id="user-id" type="text" class="form-control" disabled>\n' +
                '                </div>\n' +
                '                <h6 class="text-center pt-3"><strong>username</strong></h6>\n' +
                '                <div class="input-group">\n' +
                '                    <input id="user-username" type="text" class="form-control" disabled>\n' +
                '                </div>\n' +
                '                <h6 class="text-center pt-3"><strong>roles</strong></h6>\n' +
                '                <select id="user-roles" class = "form-select" size = "2" multiple >\n' +
                '                </select>';
            modalBody.innerHTML = modalBodyContent;
            for (let key of keys) {
                if (key === 'roles') {
                    let rolesContent = '';
                    getAllRolesJson()
                        .then(roles => {
                            for (let role of roles) {
                                rolesContent += '<option id="role-' + role.role + '" label="' + role.role + '"> </option>'

                            }
                            modalBody.querySelector('#user-' + key).innerHTML = rolesContent;
                            modalBody.querySelector('#user-' + key).setAttribute('disabled', 'disabled');
                            for (let role of user[key]) {
                                let options = modalBody.querySelector('#role-' + role.role);
                                if (options) {
                                    options.setAttribute('selected', 'selected');
                                }
                            }

                        })
                } else {
                    let attr = modalBody.querySelector('#user-' + key);
                    if (attr) {
                        attr.setAttribute('value', user[key]);
                    }
                }
            }
        }
        setSubmitModalListener();
    })
}

function modalClose() {
    let modalBody = document.querySelector('.modal-body');
    modalBody.innerHTML = '';
}

async function editUser() {
    getAllRolesJson().then(roles => {
        let userID = document.getElementById('user-id').value;
        let username = document.getElementById('user-username').value;
        let password = document.getElementById('user-password').value;
        let isEnabled = document.getElementById('user-enabled').checked;
        let enabled = false;
        if (isEnabled) {
            enabled = true;
        }
        let isADMIN = document.getElementById('role-ADMIN').selected;
        let roleADMIN = false;
        if (isADMIN) {
            roleADMIN = true;
        }
        let isUSER = document.getElementById('role-USER').selected;
        let roleUSER = false;
        if (isUSER) {
            roleUSER = true;
        }
        let rolesList = [];
        for (let role of roles) {
            if (role['role'] === 'ADMIN' && roleADMIN) {
                rolesList.push(role);
            }
            if (role['role'] === 'USER' && roleUSER) {
                rolesList.push(role);
            }
        }
        let jsonBody = JSON.stringify({
            id : userID,
            username : username,
            password : password,
            enabled : enabled,
            roles : rolesList
        })

        fetch('http://localhost:8077/admin', {
            method: 'PATCH',
            body: jsonBody,
            headers: {
                'Content-type': 'application/json; charset=UTF-8',
            },
        })
    })
    let modal = bootstrap.Modal.getInstance(document.querySelector('#modal'));
    modal.hide();
    await sleep(500);
    updateUsersTable();
}

async function deleteUser() {
    let userID = document.getElementById('user-id').value;
    fetch('http://localhost:8077/admin/' + userID, {
        method: 'DELETE',
    })
    let modal = bootstrap.Modal.getInstance(document.querySelector('#modal'));
    modal.hide();
    await sleep(500);
    updateUsersTable();
}

async function setSubmitModalListener() {
    await sleep(500);
    let submitButton = await document.getElementById('submit-button');
    if (submitButton.innerHTML === 'edit') {
        submitButton.removeEventListener('click', deleteUser);
        submitButton.addEventListener('click', editUser);
    }
    if (submitButton.innerHTML === 'delete') {
        submitButton.removeEventListener('click', editUser);
        submitButton.addEventListener('click', deleteUser);
    }
}