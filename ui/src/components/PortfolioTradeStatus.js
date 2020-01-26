import React from 'react';
import { Menu, Button } from 'semantic-ui-react'
import { connect } from 'react-redux';


class PortfolioTradeStatus extends React.Component {
    constructor(props) {
        super(props);
        this.state = { activeItem: 'open' };
    }

    getDescription(order) {
        let side = order.isBuy ? 'BUY' : 'SELL';
        let price = order.price ? Number(order.price/100).toFixed(2): 'MKT'
        return `${side} ${order.orderQty} ${order.symbol} @ ${price}`;
    }

    markToMarket(symbol,position){
       const contract = this.props.contracts[symbol];
       let pnlMultiplier = 0;
       if(contract.last_trade && contract.last_trade.price){
        if(position.position >=0){
            pnlMultiplier = position.costBasis <=  contract.last_trade.price/100? 1:-1;

        }else{
            pnlMultiplier = position.costBasis <=  contract.last_trade.price/100? -1:1;
        }
        let pnl = pnlMultiplier*Math.abs(Math.abs(position.costBasis*position.position) - Math.abs((contract.last_trade.price/100)*position.position));
        let marketValue=contract.last_trade.price*position.position;
            return {
                pnlString:(pnl<0?'-$':'$')+Math.abs(pnl).toFixed(2),
                pnl:pnl,
                marketValue:marketValue,
                marketValueString:(marketValue<0?'-$':'$')+(Math.abs(marketValue/100)).toFixed(2),
                valuationMethod:'LAST_PRICE'
            };
        }
        else{
            return {pnl:'?', marketValue:'?',valutionMethod:'unable to mark to market, no last price.'};
        }
    }

    render(props) {
        return (<div>
            <Menu attached='top' secondary>
                <Menu.Item
                    name='open'
                    active={this.state.activeItem === 'open'}
                    onClick={() => this.setState({ activeItem: 'open' })}>
                    <i className="clock outline"></i>
                    open orders
    </Menu.Item>
                <Menu.Item
                    name='closed'
                    active={this.state.activeItem === 'closed'}
                    onClick={() => this.setState({ activeItem: 'closed' })}>
                    <i className="check circle outline"></i>
                    closed orders
    </Menu.Item>
                <Menu.Item
                    name='portfolio'
                    active={this.state.activeItem === 'portfolio'}
                    onClick={() => this.setState({ activeItem: 'portfolio' })}>
                    <i className="usd"></i>
                    portfolio
    </Menu.Item>
            </Menu>
            {this.state.activeItem === 'open' &&
                (Object.keys(this.props.openOrders).length ?
                <table className='accountTable'>
                        <tbody>
                            <tr>
                                <th>order</th>
                                <th>status</th>
                                <th>qty filled</th>
                                <th>total fill cost</th>
                                <th>avg. fill price</th>
                                <th>qty on market</th>
                                <th>actions</th>
                            </tr>
                            {Object.keys(this.props.openOrders).map(key =>
                                <tr id={key}>
                                    <td>{this.getDescription(this.props.openOrders[key])}</td>
                                    <td>{this.props.openOrders[key].state}</td>
                                    <td>{this.props.openOrders[key].qtyFilled}</td>
                                    <td>{this.props.openOrders[key].qtyFilled ? Number(this.props.openOrders[key].fillCost/100).toFixed(2):'N/A'}</td>
                                    <td>{this.props.openOrders[key].qtyFilled ? Number((this.props.openOrders[key].fillCost/100)/this.props.openOrders[key].qtyFilled).toFixed(2):'N/A'}</td>
                                    <td>{this.props.openOrders[key].qtyOnMarket}</td>
                                    <td>
                                        <button>cancel</button>
                                        <button>modify</button>
                                    </td>
                                </tr>)}
                        </tbody>
                    </table>
                    : <p>no open orders</p>)}

            {this.state.activeItem === 'closed' &&
                (Object.keys(this.props.closedOrders).length ?
                <table className='accountTable'>
                <tbody>
                    <tr>
                        <th>order</th>
                        <th>status</th>
                        <th>qty filled</th>
                        <th>total fill cost</th>
                        <th>avg. fill price</th>
                    </tr>
                    {Object.keys(this.props.closedOrders).map(key =>
                        <tr id={key}>
                            <td>{this.getDescription(this.props.closedOrders[key])}</td>
                            <td>{this.props.closedOrders[key].state}</td>
                            <td>{this.props.closedOrders[key].qtyFilled}</td>
                            <td>{Number(this.props.closedOrders[key].fillCost/100).toFixed(2)}</td>
                            <td>{Number((this.props.closedOrders[key].fillCost/100)/this.props.closedOrders[key].qtyFilled).toFixed(2)}</td>
                        </tr>)}
                </tbody>
            </table>
                    : <p>no closed orders</p>)}

        {this.state.activeItem === 'portfolio' &&
                    (Object.keys(this.props.holdings).filter(key=>this.props.holdings[key].position).length ?
                <table className='accountTable'>
                <tbody>
                    <tr>
                        <th>symbol</th>
                        <th>position</th>
                        <th>cost basis</th>
                        <th>total cost</th>
                        <th>market value</th>
                        <th>PnL</th>
                        <th>actions</th>

                    </tr>
                    {Object.keys(this.props.holdings).filter(key=>this.props.holdings[key].position).map(key =>

                        {let m2m = this.markToMarket(key,this.props.holdings[key]);
                        return(<tr id={key}>
                            <td>{key}</td>
                            <td>{this.props.holdings[key].position}</td>
                            <td>{this.props.holdings[key].costBasis.toFixed(2)}</td>
                            <td>{(this.props.holdings[key].costBasis*this.props.holdings[key].position).toFixed(2)}</td>
                            <td>{m2m.marketValueString}</td>
                            <td style={{color:(m2m.pnl<0?'red':'green')}}><b>{m2m.pnlString}</b></td>
                            <td><button>close</button></td>
                    </tr>)} )}
                </tbody>
            </table>
                    : <p>no active positions</p>)}
        </div>);
    }

}

export default connect(state => ({
    openOrders: state.account.openOrders,
    closedOrders: state.account.closedOrders,
    holdings:state.account.holdings,
    contracts: state.firestore.data.contracts,
}))(PortfolioTradeStatus)