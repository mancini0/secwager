import React from 'react'
import { connect } from 'react-redux';
import { Button, Checkbox, Form, Input, Radio, Select, Message, Label, Dimmer, Loader, Icon } from 'semantic-ui-react';
import * as yup from 'yup';


import { firebase } from '../firebase/Firebase';

const { OrderEntryServicePromiseClient } = require('../proto/order_entry_grpc_web_pb.js');
const market = require('../proto/market_pb');
const orderEntry = require('../proto/order_entry_pb');





class OrderEntry extends React.Component {

    constructor(props) {
        super(props);
        this.state = ({
            errorPaths: [],
            errors: [],
            isSubmitting: false,
            wasTouched: false,
            marketSide: market.Order.OrderType.BUY,
            orderType: 'limit'
        });
    }

    options = [
        { text: 'buy', value: market.Order.OrderType.BUY },
        { text: 'sell', value: market.Order.OrderType.SELL },
    ]


    schema = yup.object().shape({
        quantity: yup.number().required().positive().integer().min(1),
        price: yup.mixed().when('orderType', {
            is: 'limit',
            then: yup.number().positive().min(.25).max(99.75).required('price is a required field for limit orders.'),
            otherwise: yup.object().nullable().notRequired()
        }),
        marketSide: yup.number().required(),
        orderType: yup.string().required()
    });


    arrayToObject = (array, keyField) =>
        array.reduce((obj, item) => {
            obj[item[keyField]] = item
            return obj
        }, {})


    handleSubmit = () => {
        this.setState({ isSubmitting: true });
        var submitOrderRequest = new orderEntry.SubmitOrderRequest();
        var orderEntryClient = new OrderEntryServicePromiseClient('http://' + process.env.ORDER_ENTRY_URL);
        var order = new market.Order();
        order.setOrderType(this.state.marketSide);
        order.setSymbol(this.state.contractId);
        order.setIsBuy((this.state.marketSide === market.Order.OrderType.BUY));
        order.setIsLimit(this.state.orderType === 'limit');
        order.setPrice(this.state.orderType === 'limit' ? Math.round(parseFloat(this.state.price) * 100) : 0);
        order.setOrderQty(this.state.quantity);
        firebase.auth().currentUser.getIdToken(true)
            .then(idToken => {
                submitOrderRequest.setAuthToken(idToken);
                submitOrderRequest.setOrder(order);
                return orderEntryClient.submitOrder(submitOrderRequest);
            })
            .then(submitOrderResponse =>console.log('Hooray:'+JSON.stringify(submitOrderResponse)))
            .catch(err => 'Whoopsies:'+console.log(err))
            .finally(this.setState({ isSubmitting: false }))
    };



    handleChange = (e, component) => {
        this.setState({ [component.name]: component.value, wasTouched: true }, () => this.schema.validate(this.state, { abortEarly: false })
            .then(valid => this.setState({ errorPaths: [], errors: [] })) //clear any hanging validation errors
            .catch(err => this.setState({ errors: err.errors, errorPaths: err.inner.map(i => i.path) })))
    };


    render(props) {
        return (
            <Dimmer.Dimmable dimmed={this.state.isSubmitting}>
                <Dimmer active={this.state.isSubmitting}>
                    <Icon size='massive' color='purple' name='soccer' loading />
                </Dimmer>
                <div className='row'>
                    <div className='col'>
                        <p><b>{this.props.instrument.getDescription()}</b></p>
                        <Form size='mini'>
                            <Form.Field error={this.state.errorPaths.includes('marketSide')} name="marketSide" defaultValue={market.Order.OrderType.BUY} control={Select} label='Market side' options={this.options} placeholder='market side' onChange={this.handleChange} />
                            <Form.Field error={this.state.errorPaths.includes('quantity')} type='number' name="quantity" control={Input} label='Quantity' placeholder='quantity' onChange={this.handleChange} />

                            <Form.Group>
                                <Form.Field error={this.state.errorPaths.includes('orderType')} >
                                    <Radio
                                        label='market'
                                        name='orderType'
                                        value='market'
                                        checked={this.state.orderType === 'market'}
                                        onChange={this.handleChange}
                                    />
                                </Form.Field>
                                <Form.Field error={this.state.errorPaths.includes('orderType')}>
                                    <Radio
                                        label='limit'
                                        name='orderType'
                                        value='limit'
                                        checked={this.state.orderType === 'limit'}
                                        onChange={this.handleChange}
                                    />
                                </Form.Field>
                            </Form.Group>
                            <Form.Field error={this.state.errorPaths.includes('price')} disabled={this.state.orderType === 'market'} name='price' control={Input} label='Price' placeholder='price' onChange={this.handleChange} />
                            <Button color='green' disabled={!this.state.wasTouched || this.state.errorPaths.length || !this.props.user} onClick={this.handleSubmit}>Submit</Button>
                        </Form>
                    </div>
                    <div className='col'>
                        {(this.state.errors.length || !this.props.user) &&
                            <div>
                                {/**<h4>please correct these form issues:</h4>**/}
                                <ul>
                                    {this.state.errors.map(e => <li>{e}</li>)}
                                    {!this.props.user && <li>you must log in before submitting an order</li>}
                                </ul>
                            </div>}

                    </div>
                </div>
            </Dimmer.Dimmable>
        )
    }
}

export default connect((state) => ({
    user: state.auth.uid
}))(OrderEntry)