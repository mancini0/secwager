import React from 'react';
import { connect } from 'react-redux';
import { startLogout } from '../actions/AuthActions'
import { Link } from 'react-router-dom';

let Header = (props) => {
    return (
        <div className='row'>
            <div className='col'>
                <b>SECWAGER</b> <br /> <i>securitized sports wagers</i>
            </div>
            {props.user
                ? <React.Fragment>

                    <div className='col'>
                        {props.user.email}
                    </div>
                    <div className='col'>
                        <Link
                            to=""
                            style={{
                                color: "blue"
                            }}
                            onClick={props.logout}>logout</Link>
                    </div>
                    <div className='col'>
                        <Link
                            to=""
                            style={{
                                color: "blue"
                            }}>deposit</Link>
                    </div>
                    <div className='col'>
                        <Link
                            to=""
                            style={{
                                color: "blue"
                            }}>withdrawal</Link>
                    </div>
                </React.Fragment>
                : <div className="col">
                    <Link
                        style={{
                            color: "gold"
                        }}
                        to="/login">
                        <u>login</u>
                    </Link>
                </div>}
        </div>
    )
}

const mapStateToProps = (state) => ({ user: state.auth.uid });

const mapDispatchToProps = (dispatch) => {
    return {
        logout: () => {
            dispatch(startLogout());
        }
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(Header);
