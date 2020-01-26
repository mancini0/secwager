import React from 'react';
import { getDepthByIsin } from '../selectors'
import { connect } from 'react-redux';

const DepthBook = (props) => (  

    <div>

      <h4 style={{"text-align":"center"}}>market depth</h4>
      <div className='row'>
       <div className='col spaced'>
        <table>
        <caption style={{ color: 'red', fontSize: 'large' }}>asks</caption>
           <tbody>
          <tr>
            <th>price</th>
            <th>size</th>
          </tr>
          {props.depth.getAskPricesList().map((ask, i) =>
  
            <tr key={ask}>
              <td>{(ask / 100).toFixed(2)}</td>
              <td>{props.depth.getAskQtysList()[i]}</td>
            </tr>)}
            </tbody>
        </table>
       </div>
       <div className='col spaced'>
        <table>
        <caption style={{ color: 'green', fontSize: 'large' }}>bids</caption>
        <tbody>
          <tr>
            <th>price</th>
            <th>size</th>
          </tr>
        {props.depth.getBidPricesList().map((bid, i) =>

                   <tr key={bid}>
                     <td>{(bid / 100).toFixed(2)}</td>
                     <td>{props.depth.getBidQtysList()[i]}</td>
                   </tr>)}
        </tbody>
        </table>
        </div>
      </div>
    </div>);

  const mapStateToProps = (state, ownProps) => ({
      depth: getDepthByIsin(state, ownProps.isin),
  })

  export default connect(mapStateToProps)(DepthBook);