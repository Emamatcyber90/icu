// © 2017 and later: Unicode, Inc. and others.
// License & terms of use: http://www.unicode.org/copyright.html#License
package newapi.impl;

import com.ibm.icu.text.DecimalFormatSymbols;
import com.ibm.icu.util.Currency;
import com.ibm.icu.util.ULocale;

public class CustomSymbolCurrency extends Currency {
  private String symbol1;
  private String symbol2;

  public static Currency resolve(Currency currency, ULocale locale, DecimalFormatSymbols symbols) {
    if (currency == null) {
      currency = symbols.getCurrency();
    }
    String currency1Sym = symbols.getCurrencySymbol();
    String currency2Sym = symbols.getInternationalCurrencySymbol();
    if (currency == null) {
      return new CustomSymbolCurrency("XXX", currency1Sym, currency2Sym);
    }
    if (!currency.equals(symbols.getCurrency())) {
      return currency;
    }
    String currency1 = currency.getName(symbols.getULocale(), Currency.SYMBOL_NAME, null);
    String currency2 = currency.getCurrencyCode();
    if (!currency1.equals(currency1Sym) || !currency2.equals(currency2Sym)) {
      return new CustomSymbolCurrency(currency2, currency1Sym, currency2Sym);
    }
    return currency;
  }

  public CustomSymbolCurrency(String isoCode, String currency1Sym, String currency2Sym) {
    super(isoCode);
    this.symbol1 = currency1Sym;
    this.symbol2 = currency2Sym;
  }

  @Override
  public String getName(ULocale locale, int nameStyle, boolean[] isChoiceFormat) {
    if (nameStyle == SYMBOL_NAME) {
      return symbol1;
    }
    return super.getName(locale, nameStyle, isChoiceFormat);
  }

  @Override
  public String getName(
      ULocale locale, int nameStyle, String pluralCount, boolean[] isChoiceFormat) {
    if (nameStyle == PLURAL_LONG_NAME && subType.equals("XXX")) {
      // Plural in absence of a currency should return the symbol
      return symbol1;
    }
    return super.getName(locale, nameStyle, pluralCount, isChoiceFormat);
  }

  @Override
  public String getCurrencyCode() {
    return symbol2;
  }
}
