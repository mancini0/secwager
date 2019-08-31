import React from 'react'
import { Button,Icon,Modal } from 'semantic-ui-react'
import { SignIn } from './SignIn';
import {Redirect, withRouter} from 'react-router-dom';
import {connect} from 'react-redux';

class LoginModal extends React.Component {
    constructor(props){
        super(props)
        this.state={open:true};
    }


render = (props) => (
  <Modal open={this.state.open}>
    <Modal.Header>Please register or login.</Modal.Header>
    <Modal.Content image>
      <Modal.Description>
        <p>Select an authentication option below.</p>
        <SignIn/>
        <Modal.Actions>
      <Button color='purple' onClick={()=>this.props.history.push("/")}>
        <Icon name='remove' /> close
      </Button>
    </Modal.Actions>
      </Modal.Description>
    </Modal.Content>
  </Modal>);

}
const mapStateToProps = (state) =>({uid:state.auth.uid});

export default withRouter(connect(mapStateToProps)(LoginModal));

