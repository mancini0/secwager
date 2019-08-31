import React from 'react';
import {connect} from 'react-redux';
import {startLogout} from '../actions/AuthActions'
import {Link} from 'react-router-dom';
import {Icon} from 'semantic-ui-react';

let Header = (props) => {
    return <div className='header-container'>
       <div className='header-element'>
       <Icon name='chart line' size='big' />
       <div>SECWAGER</div>
        </div> 
       {props.user && 
       <div>
       <div className='header-element'>
            logged in as: {props.user.displayName}
       </div>
       <div className='header-element'>
           <Link to="" style={{color:"blue"}} onClick={props.logout}>logout</Link> 
        </div>
        <div className='header-element'>
           <Link to="" style={{color:"blue"}}>deposit</Link> 
        </div>
        <div className='header-element'>
           <Link to="" style={{color:"blue"}}>withdrawal</Link> 
        </div>
       </div> 
    }

        {!props.user && 
        <div className="header-element">
            <Link style={{color:"blue"}} to="/login"><u>login</u></Link>
        </div>}
    </div>
    
}


const mapStateToProps = (state) => ({user: state.auth.uid});

const mapDispatchToProps = (dispatch) => {
    return {
        logout: () => {
            dispatch(startLogout());
        }
    };
};


export default connect(mapStateToProps, mapDispatchToProps)(Header);
