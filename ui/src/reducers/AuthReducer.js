import {LOGIN, LOGOUT} from '../actions/AuthActions';


    let initialState ={
        uid:undefined
    };

export default (state = initialState, action) =>{
    switch(action.type){
        case LOGIN:
            return {
                uid:action.uid 
            };
        
        case LOGOUT:
            return {
                uid:undefined
            };
            
        default:
            return state;
        }
}