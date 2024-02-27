package nu.revitalized.revitalizedwebshop;

public class Checklist {
    // ORDER
    // TODO: Relations:
    // TODO: Relation - Products
    // TODO: Relation - ShippingDetails
    // TODO: Relation - User


    // Overig
    // TODO: User (Athenticated) Get ShippingDetails BY ID
    // TODO: De url van authenticate aanpassen op users/authenticate
    // TODO: Zorgen dat de reviews ook worden toegevoegd als je de super ophaalt
    // TODO: -- Door in de productDto de reviews toe te voegen en ze in de transfer methods in product service erin knallen
    // TODO: Foto's toevoegen
    // TODO: Alles nalopen op imports etc.
    // TODO: checken in de services of de methods bij de owner of target kant staan (discount voorbeeld)
    // TODO: de shipping details op een andere URL zetten /users/shipping-details


    // Rowan
    // TODO: Vragen hoe de foto's geimporteerd worden


    // assign products to order
    // remove products from order

    // assign order to user
    // remove order from user

    // assign shipping details to order
    // update shipping details from order
    // patch shipping details from order
    // remove shipping details from order



    // USER (auth)
    // Get all personal orders (wellicht met een shortdto -> id, datum, prijs)
    // get user specific order (met alle data)

    // Create Order
    // Add products to order
    // Remove products from order

    // assign shippingDetails to order
    // post new shipping details for order
    // update shipping details for order?

    // Finish order Send/Pay order (hier de discount bij)
    // Deze kan niet verzonden worden indien er geen shippingdetails bij zit!
    // En ook niet als er geen producten in zitten
    // En ook niet als de discount niet geldig is
}
