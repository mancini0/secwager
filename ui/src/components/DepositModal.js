import React from 'react'
import { Button, Header, Image, Icon, Modal } from 'semantic-ui-react'
import { DEPOSIT_INFO_MSG } from '../messages'
import PlaidLink from 'react-plaid-link';

class DepositModal extends React.Component {

    constructor(props) {
        super(props);
    }


    handlePlaidSuccess(a, b) {
        console.log(JSON.stringify(a));
        console.log(JSON.stringify(b));
    }


    render = (props) => (
        <Modal
            closeIcon
            trigger={this.props.trigger}>
            <Modal.Header>{'Deposit funds'}</Modal.Header>
            <Modal.Content>
                <div>
                    <h5>{DEPOSIT_INFO_MSG}</h5>
                    <table>
                        <tbody>
                            <tr>
                                <th>method</th>
                                <th>funds availability</th>
                                <th>deposit limit</th>
                                <th>fee</th>
                                <th></th>
                            </tr>
                            <tr>
                                <td>transfer funds via bank ACH</td>
                                <td>2-5 days</td>
                                <td>unlimited</td>
                                <td>0.80% of deposit amount, capped at $5</td>
                                <td>
                                    <PlaidLink
                                        clientName="SECWAGER"
                                        env="sandbox"
                                        product={["auth", "transactions"]}
                                        publicKey="42ad9756c153f56d84c1e96328dc45"
                                        onExit={this.handleOnExit}
                                        onSuccess={this.handlePlaidSuccess}>
                                        <Button small color='blue'>
                                            use bank
                                            </Button>
                                    </PlaidLink>
                                </td>
                            </tr>
                            <tr>
                                <td>credit card</td>
                                <td>instant</td>
                                <td>$1000 per transaction</td>
                                <td>2.9% of deposit amount, plus 30 cents</td>
                                <td>
                                    <Button onClick={() => console.log('gucci boi credit')} small color='blue'>
                                        use credit card
                                        </Button>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </Modal.Content>
        </Modal>
    );

}



export default DepositModal;