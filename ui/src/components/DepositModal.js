import React from 'react'
import { Button, Header, Image, Icon, Modal } from 'semantic-ui-react'

class DepositModal extends React.Component {

    constructor(props) {
        super(props);
    }

    render = (props) => (
        <Modal
            closeIcon
            trigger={this.props.trigger}>
            <Modal.Header>{'Deposit funds'}</Modal.Header>
            <Modal.Content>
                <div>
                    <h5>WHAT UP</h5>
                </div>
            </Modal.Content>
        </Modal>
    );

}



export default DepositModal;