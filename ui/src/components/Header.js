import React from 'react';
import { connect } from 'react-redux';
import { startLogout } from '../actions/AuthActions'
import { Link } from 'react-router-dom';
import { Button, Header, Image, Modal } from 'semantic-ui-react'
import { SignIn } from './SignIn';
import DepositModal from './DepositModal';

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
                        <DepositModal trigger={<u>deposit</u>} />
                    </div>
                    <div className='col'>
                        <Link
                            to=""
                            style={{
                                color: "blue"
                            }}>withdrawal</Link>
                    </div>
                </React.Fragment>
                : <div className="col clickable" >
                    <Modal trigger={<u>login</u>}>
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

const mapStateToProps = (state) => ({ user: state.auth.uid });

const mapDispatchToProps = (dispatch) => {
    return {
        logout: () => {
            dispatch(startLogout());
        }
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(HeaderBar);
