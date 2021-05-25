import {LOGIN, LOGOUT} from '../actions/AuthActions';


    let initialState ={
        uid:undefined,
        address: undefined
    };

export default (state = initialState, action) =>{
    switch(action.type){
        case LOGIN:
            return {
                uid:action.uid,
                address: "gucciboi"
            };
        
        case LOGOUT:
            return {
                uid:undefined,
                address: undefined
            };
            
        default:
            return state;
        }
}