const baseURI = "http://localhost:8080/";

const debug = true;

const post = (resource, body) => {
    return request(resource,'POST',body);
};

const put = (resource, body) => {
    return request(resource,'PUT',body);
};

const del = async (resource) => { 
    const encoded = encodeURI(resource);
    return await bodylessRequest(encoded,'DELETE');
}

const get = (resource) => { 
    return bodylessRequest(resource,'GET');
}

const bodylessRequest = async (resource,method) => { 
    const requestConfig = {
        method: method,
        headers: {
            "Accept":"application/json"
        }
    }
    const uri = `${baseURI}/${resource}`;
    if(debug)
        console.info(`${method} ${resource} ${uri}`);
    return await fetch(uri, requestConfig);

}
const request = (resource, method, body) => {
    if (debug)
        console.log('Raw body',body);
    const payload = JSON.stringify(body);
    if (debug)
        console.log('Stringified body',payload);
    const requestConfig = {
        method: method,
        body: payload,
        headers: {
            "Content-type":"application/json"
        }
    }
    const uri = encodeURI(`${baseURI}/${resource}`);
    if(debug)
        console.info(`${method} ${resource} ${uri}`, payload);
    return fetch(uri, requestConfig);
};

export { post,put,del,get };