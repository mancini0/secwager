import React from 'react';
import { connect } from 'react-redux';
import { startLogout } from '../actions/AuthActions'
import { Link } from 'react-router-dom';
import { Button, Header, Image, Modal } from 'semantic-ui-react'
import { SignIn } from './SignIn';
import DepositModal from './DepositModal'

class HeaderBar extends React.Component {

    constructor(props) {
        super(props);
    }

    render = (props) => (
        <div className='row'>
            <div className='col'>
                <b>SECWAGER</b> <br /> <i>securitized sports wagers</i>
            </div>
            {this.props.user
                ? <React.Fragment>

                    <div className='col'>
                        {this.props.user.email}
                    </div>
                    <div className='col'>
                        <Link
                            to=""
                            style={{
                                color: "blue"
                            }}
                            onClick={this.props.logout}>logout</Link>
                    </div>
                    <div className='col clickable'>
                        <DepositModal address={this.props.address} trigger={<Link
                            to=""
                            style={{
                                color: "blue"
                            }}>deposit</Link>} />
                    </div>
                </React.Fragment>
                : <div className="col clickable" >
                    <Modal trigger={<Link
                        to=""
                        style={{
                            color: "blue"
                        }}>login</Link>}>
                        <Modal.Header>Please register or login.</Modal.Header>
                        <Modal.Content image>
                            <Modal.Description>
                                <p>Select an authentication option below.</p>
                                <SignIn />
                            </Modal.Description>
                        </Modal.Content>
                    </Modal>

                </div>}
        </div>
    )


}

const mapStateToProps = (state) => ({ user: state.auth.uid,
                                      address: state.auth.address });

const mapDispatchToProps = (dispatch) => {
    return {
        logout: () => {
            dispatch(startLogout());
        }
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(HeaderBar);
